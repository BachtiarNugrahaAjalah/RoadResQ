package com.app.rrq.model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rrq.data.local.SessionManager
import com.app.rrq.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    /** Upload foto profil ke Firebase Storage */
    fun uploadFoto(context: Context, imageUri: Uri, onSuccess: (String) -> Unit) {
        val session = SessionManager(context)
        val uid = session.getUid()
        if (uid.isBlank()) {
            _state.value = ProfilState.Error("Sesi tidak valid, silakan login ulang")
            return
        }
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            val result = repository.uploadFotoProfil(context, uid, imageUri)
            if (result.isSuccess) {
                val url = result.getOrDefault("")
                session.savePhotoUrl(url)
                _state.value = ProfilState.Success
                onSuccess(url)
            } else {
                val errMsg = result.exceptionOrNull()?.message ?: "Gagal mengunggah foto"
                // Pesan error yang lebih ramah pengguna
                val friendlyMsg = when {
                    errMsg.contains("object-not-found", ignoreCase = true) ||
                    errMsg.contains("does not exist", ignoreCase = true) ->
                        "Gagal menyimpan foto. Pastikan koneksi internet stabil dan coba lagi."
                    errMsg.contains("unauthorized", ignoreCase = true) ||
                    errMsg.contains("permission", ignoreCase = true) ->
                        "Tidak memiliki izin untuk mengupload foto."
                    errMsg.contains("canceled", ignoreCase = true) ->
                        "Upload foto dibatalkan."
                    else -> errMsg
                }
                _state.value = ProfilState.Error(friendlyMsg)
            }
        }
    }
}
