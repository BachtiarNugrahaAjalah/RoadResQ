package com.app.rrq.ui.pages.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LaporanViewModel : ViewModel() {

    private val repository = LaporanRepository()

    // State form BuatLaporan
    var judul by mutableStateOf("")
        private set
    var kategori by mutableStateOf("")
        private set
    var urgensi by mutableStateOf("Sedang")
        private set
    var lokasi by mutableStateOf("")
        private set
    var deskripsi by mutableStateOf("")
        private set
    var gambar by mutableStateOf("")
        private set
    var expanded by mutableStateOf(false)
        private set
    var isLoading by mutableStateOf(false)
        private set

    // State daftar laporan
    var laporanList by mutableStateOf<List<Laporan>>(emptyList())
        private set

    init {
        muatSemuaLaporan()
    }

    fun onJudulChange(value: String) { judul = value }
    fun onKategoriChange(value: String) { kategori = value }
    fun onUrgensiChange(value: String) { urgensi = value }
    fun onLokasiChange(value: String) { lokasi = value }
    fun onDeskripsiChange(value: String) { deskripsi = value }
    fun onGambarChange(value: String) { gambar = value }
    fun onExpandedChange(value: Boolean) { expanded = value }

    fun muatSemuaLaporan() {
        repository.getSemuaLaporan { list ->
            laporanList = list
        }
    }

    fun kirimLaporan(onSuccess: () -> Unit) {
        if (judul.isBlank() || kategori.isBlank() || lokasi.isBlank()) return

        val tanggal = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val laporan = Laporan(
            judulLaporan = judul,
            kategoriKerusakan = kategori,
            tingkatUrgensi = urgensi,
            lokasi = lokasi,
            deskripsi = deskripsi,
            gambarUrl = gambar,
            tanggal = tanggal
        )

        isLoading = true
        repository.kirimLaporan(
            laporan = laporan,
            onSuccess = {
                isLoading = false
                resetForm()
                onSuccess()
            },
            onError = {
                isLoading = false
            }
        )
    }

    private fun resetForm() {
        judul = ""
        kategori = ""
        urgensi = "Sedang"
        lokasi = ""
        deskripsi = ""
        gambar = ""
    }
}