package com.app.rrq.model

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.core.graphics.scale
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LaporanViewModel : ViewModel() {

    private val repository = LaporanRepository()
    private var listenerRegistration: ListenerRegistration? = null

    // State form BuatLaporan
    var judul by mutableStateOf("")
    var kategori by mutableStateOf("")
    var urgensi by mutableStateOf("Sedang")
    var lokasi by mutableStateOf("")
    var deskripsi by mutableStateOf("")
    var gambarBase64 by mutableStateOf("")
    var selectedImageUri by mutableStateOf<Uri?>(null)
    var expanded by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    // State daftar laporan
    var laporanList by mutableStateOf<List<Laporan>>(emptyList())
    var isFetching by mutableStateOf(false)

    init {
        muatSemuaLaporan()
    }

    fun onJudulChange(value: String) { judul = value }
    fun onKategoriChange(value: String) { kategori = value }
    fun onUrgensiChange(value: String) { urgensi = value }
    fun onLokasiChange(value: String) { lokasi = value }
    fun onDeskripsiChange(value: String) { deskripsi = value }
    fun onExpandedChange(value: Boolean) { expanded = value }

    fun onImageSelected(context: Context, uri: Uri) {
        selectedImageUri = uri
        viewModelScope.launch {
            val base64 = uriToBase64(context, uri)
            if (base64 != null) {
                gambarBase64 = base64
            }
        }
    }

    private suspend fun uriToBase64(context: Context, uri: Uri): String? = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return@withContext null
                val maxSize = 350
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
                Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Memuat laporan hanya untuk user yang sedang login.
     * Membersihkan list terlebih dahulu untuk memastikan tidak ada data user lain yang tertinggal.
     */
    fun muatSemuaLaporan() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            laporanList = emptyList()
            isFetching = false
            return
        }

        isFetching = true
        listenerRegistration?.remove()
        
        // Reset list agar data user lama tidak tampil saat loading data baru
        laporanList = emptyList() 

        listenerRegistration = repository.getSemuaLaporan { list ->
            // Filter tambahan di sisi client untuk memastikan keamanan data
            laporanList = list.filter { it.userId == uid }
            isFetching = false
        }
    }

    fun kirimLaporan(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (judul.isBlank() || kategori.isBlank() || lokasi.isBlank() || gambarBase64.isBlank()) {
            onError("Harap isi semua data wajib dan foto")
            return
        }

        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUid == null) {
            onError("User tidak teridentifikasi, silakan login kembali")
            return
        }

        viewModelScope.launch {
            isLoading = true
            val tanggal = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val laporan = Laporan(
                userId = currentUid,
                judulLaporan = judul,
                kategoriKerusakan = kategori,
                tingkatUrgensi = urgensi,
                lokasi = lokasi,
                deskripsi = deskripsi,
                gambarUrl = gambarBase64,
                tanggal = tanggal,
                status = "Menunggu"
            )

            val result = repository.simpanLaporan(laporan)
            isLoading = false
            if (result.isSuccess) {
                resetForm()
                onSuccess()
            } else {
                onError(result.exceptionOrNull()?.message ?: "Gagal mengirim laporan")
            }
        }
    }

    private fun resetForm() {
        judul = ""
        kategori = ""
        lokasi = ""
        deskripsi = ""
        gambarBase64 = ""
        selectedImageUri = null
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
