package com.app.rrq.viewmodel.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.data.Pengguna
import com.app.rrq.model.usecase.GetPenggunasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfilState {
    object Loading : ProfilState()
    data class Success(val pengguna: Pengguna) : ProfilState()
    data class Error(val message: String) : ProfilState()
}

class ProfilViewModel(
    private val getPenggunasUseCase: GetPenggunasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfilState>(ProfilState.Loading)
    val uiState: StateFlow<ProfilState> = _uiState.asStateFlow()

    fun loadProfil(userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfilState.Loading
            try {
                val penggunas = getPenggunasUseCase()
                val user = penggunas.find { it.id == userId }
                
                if (user != null) {
                    _uiState.value = ProfilState.Success(user)
                } else {
                    _uiState.value = ProfilState.Error("Pengguna tidak ditemukan")
                }
            } catch (e: Exception) {
                _uiState.value = ProfilState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    companion object {
        fun provideFactory(
            getPenggunasUseCase: GetPenggunasUseCase
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ProfilViewModel::class.java)) {
                    return ProfilViewModel(getPenggunasUseCase) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
