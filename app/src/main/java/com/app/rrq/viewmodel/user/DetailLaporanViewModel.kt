package com.app.rrq.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.data.Laporan
import com.app.rrq.model.usecase.GetLaporanByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DetailLaporanState {
    object Loading : DetailLaporanState()
    data class Success(val laporan: Laporan) : DetailLaporanState()
    data class Error(val message: String) : DetailLaporanState()
}

class DetailLaporanViewModel(
    private val getLaporanByIdUseCase: GetLaporanByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailLaporanState>(DetailLaporanState.Loading)
    val uiState: StateFlow<DetailLaporanState> = _uiState.asStateFlow()

    fun fetchLaporanById(id: String) {
        viewModelScope.launch {
            _uiState.value = DetailLaporanState.Loading
            try {
                val laporan = getLaporanByIdUseCase(id)
                if (laporan != null) {
                    _uiState.value = DetailLaporanState.Success(laporan)
                } else {
                    _uiState.value = DetailLaporanState.Error("Laporan tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = DetailLaporanState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    companion object {
        fun provideFactory(
            getLaporanByIdUseCase: GetLaporanByIdUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DetailLaporanViewModel::class.java)) {
                    return DetailLaporanViewModel(getLaporanByIdUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
