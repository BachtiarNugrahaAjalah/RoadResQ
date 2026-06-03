package com.app.rrq.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.data.Laporan
import com.app.rrq.model.usecase.GetLaporanByIdUseCase
import com.app.rrq.model.usecase.GetLaporansUseCase
import com.app.rrq.model.usecase.UpdateStatusLaporanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class VerifikasiLaporanState {
    object Loading : VerifikasiLaporanState()
    data class Success(val laporan: Laporan) : VerifikasiLaporanState()
    data class Error(val message: String) : VerifikasiLaporanState()
}

class VerifikasiLaporanViewModel(
    private val getLaporanByIdUseCase: GetLaporanByIdUseCase,
    private val getLaporansUseCase: GetLaporansUseCase,
    private val updateStatusLaporanUseCase: UpdateStatusLaporanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<VerifikasiLaporanState>(VerifikasiLaporanState.Loading)
    val uiState: StateFlow<VerifikasiLaporanState> = _uiState.asStateFlow()

    private val _updateStatus = MutableStateFlow<Boolean?>(null)
    val updateStatus: StateFlow<Boolean?> = _updateStatus.asStateFlow()

    fun fetchLaporanById(id: String) {
        viewModelScope.launch {
            _uiState.value = VerifikasiLaporanState.Loading
            try {
                val laporan = getLaporanByIdUseCase(id) ?: getLaporansUseCase().firstOrNull()
                if (laporan != null) {
                    _uiState.value = VerifikasiLaporanState.Success(laporan)
                } else {
                    _uiState.value = VerifikasiLaporanState.Error("Laporan tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = VerifikasiLaporanState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun updateLaporanStatus(id: String, status: String) {
        viewModelScope.launch {
            _updateStatus.value = null
            try {
                val success = updateStatusLaporanUseCase(id, status)
                _updateStatus.value = success
                if (success) {
                    fetchLaporanById(id)
                }
            } catch (e: Exception) {
                _updateStatus.value = false
            }
        }
    }

    fun resetUpdateStatus() {
        _updateStatus.value = null
    }

    companion object {
        fun provideFactory(
            getLaporanByIdUseCase: GetLaporanByIdUseCase,
            getLaporansUseCase: GetLaporansUseCase,
            updateStatusLaporanUseCase: UpdateStatusLaporanUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(VerifikasiLaporanViewModel::class.java)) {
                    return VerifikasiLaporanViewModel(
                        getLaporanByIdUseCase,
                        getLaporansUseCase,
                        updateStatusLaporanUseCase
                    ) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
