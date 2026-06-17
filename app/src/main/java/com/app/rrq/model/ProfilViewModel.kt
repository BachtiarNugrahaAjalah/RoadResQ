package com.app.rrq.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rrq.data.local.SessionManager
import com.app.rrq.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

sealed class ProfilState {
    object Idle : ProfilState()
    object Loading : ProfilState()
    object Success : ProfilState()
    data class Error(val message: String) : ProfilState()
}

class ProfilViewModel : ViewModel() {

    private val repository = ProfileRepository()

    private val _state = MutableStateFlow<ProfilState>(ProfilState.Idle)
    val state: StateFlow<ProfilState> = _state.asStateFlow()

    fun resetState() {
        _state.value = ProfilState.Idle
    }

    /** Update nama pengguna */
    fun updateNama(context: Context, namaBaru: String) {
        val session = SessionManager(context)
        val uid = session.getUid()
        if (uid.isBlank()) {
            _state.value = ProfilState.Error("Sesi tidak valid")
            return
        }
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            val result = repository.updateNama(uid, namaBaru)
            if (result.isSuccess) {
                session.saveNama(namaBaru)
                _state.value = ProfilState.Success
            } else {
                _state.value = ProfilState.Error(
                    result.exceptionOrNull()?.message ?: "Gagal memperbarui nama"
                )
            }
        }
    }

    /**
     * Update email – memerlukan password lama untuk re-autentikasi.
     * Firebase akan mengirim verifikasi email, sehingga email aktif berubah
     * setelah dikonfirmasi oleh pengguna.
     */
    fun updateEmail(context: Context, emailBaru: String, password: String) {
        val session = SessionManager(context)
        val uid = session.getUid()
        val emailLama = session.getEmail()
        if (uid.isBlank()) {
            _state.value = ProfilState.Error("Sesi tidak valid")
            return
        }
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            val result = repository.updateEmail(emailLama, password, emailBaru, uid)
            if (result.isSuccess) {
                session.saveEmail(emailBaru)
                _state.value = ProfilState.Success
            } else {
                val msg = result.exceptionOrNull()?.message ?: "Gagal memperbarui email"
                _state.value = ProfilState.Error(
                    when {
                        msg.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) ||
                        msg.contains("wrong-password", ignoreCase = true) ->
                            "Password yang Anda masukkan salah"
                        msg.contains("email-already-in-use", ignoreCase = true) ->
                            "Email sudah digunakan oleh akun lain"
                        else -> msg
                    }
                )
            }
        }
    }

    /** Update password – memerlukan password lama untuk re-autentikasi */
    fun updatePassword(context: Context, passwordLama: String, passwordBaru: String) {
        val session = SessionManager(context)
        val email = session.getEmail()
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            val result = repository.updatePassword(email, passwordLama, passwordBaru)
            if (result.isSuccess) {
                _state.value = ProfilState.Success
            } else {
                val msg = result.exceptionOrNull()?.message ?: "Gagal memperbarui password"
                _state.value = ProfilState.Error(
                    when {
                        msg.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) ||
                        msg.contains("wrong-password", ignoreCase = true) ->
                            "Kata sandi lama yang Anda masukkan salah"
                        else -> msg
                    }
                )
            }
        }
    }

    /**
     * Konversi foto ke base64 di background, lalu simpan ke Firestore.
     * Tidak memerlukan Firebase Storage — konsisten dengan pendekatan gambar laporan.
     */
    fun uploadFoto(context: Context, imageUri: Uri, onSuccess: (String) -> Unit) {
        val session = SessionManager(context)
        val uid = session.getUid()
        if (uid.isBlank()) {
            _state.value = ProfilState.Error("Sesi tidak valid, silakan login ulang")
            return
        }
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            val base64 = uriToBase64(context, imageUri)
            if (base64 == null) {
                _state.value = ProfilState.Error("Gagal memproses gambar, coba pilih foto lain")
                return@launch
            }
            val result = repository.simpanFotoBase64(uid, base64)
            if (result.isSuccess) {
                session.savePhotoUrl(base64)
                _state.value = ProfilState.Success
                onSuccess(base64)
            } else {
                _state.value = ProfilState.Error(
                    result.exceptionOrNull()?.message ?: "Gagal menyimpan foto"
                )
            }
        }
    }

    /** Konversi URI gambar menjadi string base64 yang sudah di-resize dan dikompresi */
    private suspend fun uriToBase64(context: Context, uri: Uri): String? =
        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val originalBitmap = BitmapFactory.decodeStream(inputStream)
                        ?: return@withContext null
                    val maxSize = 400
                    val width = originalBitmap.width
                    val height = originalBitmap.height
                    val bitmap = if (width > maxSize || height > maxSize) {
                        val ratio = width.toFloat() / height.toFloat()
                        val newWidth = if (width > height) maxSize else (maxSize * ratio).toInt()
                        val newHeight = if (height > width) maxSize else (maxSize / ratio).toInt()
                        originalBitmap.scale(newWidth, newHeight, true)
                    } else {
                        originalBitmap
                    }
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream)
                    Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
                }
            } catch (e: Exception) {
                null
            }
        }
}
