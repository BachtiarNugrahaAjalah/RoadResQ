package com.app.rrq.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.data.Laporan
import com.app.rrq.model.usecase.GetLaporansByUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RiwayatLaporanState {
    object Loading : RiwayatLaporanState()
    data class Success(val laporans: List<Laporan>) : RiwayatLaporanState()
    data class Error(val message: String) : RiwayatLaporanState()
}

class RiwayatLaporanViewModel(
    private val getLaporansByUserUseCase: GetLaporansByUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RiwayatLaporanState>(RiwayatLaporanState.Loading)
    val uiState: StateFlow<RiwayatLaporanState> = _uiState.asStateFlow()

    fun fetchRiwayatLaporan(userId: String) {
        viewModelScope.launch {
            _uiState.value = RiwayatLaporanState.Loading
            try {
                val result = getLaporansByUserUseCase(userId)
                _uiState.value = RiwayatLaporanState.Success(result)
            } catch (e: Exception) {
                _uiState.value = RiwayatLaporanState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    companion object {
        fun provideFactory(
            getLaporansByUserUseCase: GetLaporansByUserUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(RiwayatLaporanViewModel::class.java)) {
                    return RiwayatLaporanViewModel(getLaporansByUserUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
