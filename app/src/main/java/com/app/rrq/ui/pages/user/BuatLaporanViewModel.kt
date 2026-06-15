package com.app.rrq.ui.pages.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BuatLaporanViewModel : ViewModel() {
    var judul by mutableStateOf("")
    var kategori by mutableStateOf("")
    var urgensi by mutableStateOf("Sedang")
    var lokasi by mutableStateOf("")
    var deskripsi by mutableStateOf("")
    var expanded by mutableStateOf(false)
    var isLoading by mutableStateOf(false)

    fun onJudulChange(newValue: String) { judul = newValue }
    fun onKategoriChange(newValue: String) { kategori = newValue }
    fun onUrgensiChange(newValue: String) { urgensi = newValue }
    fun onLokasiChange(newValue: String) { lokasi = newValue }
    fun onDeskripsiChange(newValue: String) { deskripsi = newValue }
    fun onExpandedChange(newValue: Boolean) { expanded = newValue }

    fun kirimLaporan(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            delay(2000)
            isLoading = false
            onSuccess()
            resetFields()
        }
    }

    private fun resetFields() {
        judul = ""
        kategori = ""
        urgensi = "Sedang"
        lokasi = ""
        deskripsi = ""
    }
}