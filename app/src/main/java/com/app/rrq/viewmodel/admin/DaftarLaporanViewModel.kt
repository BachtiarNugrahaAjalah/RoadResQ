package com.app.rrq.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.data.Laporan
import com.app.rrq.model.usecase.GetLaporansUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DaftarLaporanState {
    object Loading : DaftarLaporanState()
    data class Success(val laporans: List<Laporan>) : DaftarLaporanState()
    data class Error(val message: String) : DaftarLaporanState()
}

class DaftarLaporanViewModel(
    private val getLaporansUseCase: GetLaporansUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DaftarLaporanState>(DaftarLaporanState.Loading)
    val uiState: StateFlow<DaftarLaporanState> = _uiState.asStateFlow()

    init {
        fetchLaporans()
    }

    fun fetchLaporans() {
        viewModelScope.launch {
            _uiState.value = DaftarLaporanState.Loading
            try {
                val result = getLaporansUseCase()
                _uiState.value = DaftarLaporanState.Success(result)
            } catch (e: Exception) {
                _uiState.value = DaftarLaporanState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    companion object {
        fun provideFactory(
            getLaporansUseCase: GetLaporansUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DaftarLaporanViewModel::class.java)) {
                    return DaftarLaporanViewModel(getLaporansUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
