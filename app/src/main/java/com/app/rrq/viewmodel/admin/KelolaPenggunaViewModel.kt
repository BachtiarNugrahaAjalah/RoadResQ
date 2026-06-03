package com.app.rrq.viewmodel.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.data.Pengguna
import com.app.rrq.model.usecase.GetPenggunasUseCase
import com.app.rrq.model.usecase.UpdateStatusPenggunaUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class KelolaPenggunaState {
    object Loading : KelolaPenggunaState()
    data class Success(val penggunas: List<Pengguna>) : KelolaPenggunaState()
    data class Error(val message: String) : KelolaPenggunaState()
}

class KelolaPenggunaViewModel(
    private val getPenggunasUseCase: GetPenggunasUseCase,
    private val updateStatusPenggunaUseCase: UpdateStatusPenggunaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<KelolaPenggunaState>(KelolaPenggunaState.Loading)
    val uiState: StateFlow<KelolaPenggunaState> = _uiState.asStateFlow()

    init {
        loadPenggunas()
    }

    fun loadPenggunas() {
        viewModelScope.launch {
            _uiState.value = KelolaPenggunaState.Loading
            try {
                val list = getPenggunasUseCase()
                _uiState.value = KelolaPenggunaState.Success(list)
            } catch (e: Exception) {
                _uiState.value = KelolaPenggunaState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun updateStatus(id: String, newStatus: String) {
        viewModelScope.launch {
            try {
                val success = updateStatusPenggunaUseCase(id, newStatus)
                if (success) {
                    loadPenggunas()
                } else {
                    _uiState.value = KelolaPenggunaState.Error("Gagal memperbarui status pengguna")
                }
            } catch (e: Exception) {
                _uiState.value = KelolaPenggunaState.Error(e.message ?: "Terjadi kesalahan saat memblokir")
            }
        }
    }

    companion object {
        fun provideFactory(
            getPenggunasUseCase: GetPenggunasUseCase,
            updateStatusPenggunaUseCase: UpdateStatusPenggunaUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(KelolaPenggunaViewModel::class.java)) {
                    return KelolaPenggunaViewModel(getPenggunasUseCase, updateStatusPenggunaUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
