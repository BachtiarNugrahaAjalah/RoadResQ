package com.app.rrq.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.usecase.CreateLaporanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class BuatLaporanState {
    object Idle : BuatLaporanState()
    object Loading : BuatLaporanState()
    object Success : BuatLaporanState()
    data class Error(val message: String) : BuatLaporanState()
}

class BuatLaporanViewModel(
    private val createLaporanUseCase: CreateLaporanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BuatLaporanState>(BuatLaporanState.Idle)
    val uiState: StateFlow<BuatLaporanState> = _uiState.asStateFlow()

    fun submitLaporan(
        judul: String,
        kategori: String,
        urgensi: String,
        lokasi: String,
        deskripsi: String
    ) {
        if (judul.isBlank() || kategori.isBlank() || lokasi.isBlank() || deskripsi.isBlank()) {
            _uiState.value = BuatLaporanState.Error("Semua field wajib diisi")
            return
        }

        viewModelScope.launch {
            _uiState.value = BuatLaporanState.Loading
            try {
                val success = createLaporanUseCase(judul, kategori, urgensi, lokasi, deskripsi)
                if (success) {
                    _uiState.value = BuatLaporanState.Success
                } else {
                    _uiState.value = BuatLaporanState.Error("Gagal mengirim laporan")
                }
            } catch (e: Exception) {
                _uiState.value = BuatLaporanState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetState() {
        _uiState.value = BuatLaporanState.Idle
    }

    companion object {
        fun provideFactory(
            createLaporanUseCase: CreateLaporanUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(BuatLaporanViewModel::class.java)) {
                    return BuatLaporanViewModel(createLaporanUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
