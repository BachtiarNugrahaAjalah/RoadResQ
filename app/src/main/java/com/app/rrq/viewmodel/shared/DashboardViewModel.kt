package com.app.rrq.viewmodel.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.app.rrq.model.data.Laporan
import com.app.rrq.model.usecase.GetLaporansUseCase
import com.app.rrq.model.usecase.GetLaporansByUserUseCase
import com.app.rrq.core.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(
        val totalLaporan: Int,
        val diproses: Int,
        val selesai: Int,
        val prioritasTinggi: Int,
        val laporanTerbaru: List<Laporan>,
        val allLaporans: List<Laporan> = emptyList()
    ) : DashboardState()
    data class Error(val message: String) : DashboardState()
}

class DashboardViewModel(
    private val getLaporansUseCase: GetLaporansUseCase,
    private val getLaporansByUserUseCase: GetLaporansByUserUseCase,
    private val isAdmin: Boolean
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    init {
        loadDashboardData(SessionManager.userId)
    }

    fun loadDashboardData(userId: String = SessionManager.userId) {
        viewModelScope.launch {
            _uiState.value = DashboardState.Loading
            try {
                val laporans = if (isAdmin) {
                    getLaporansUseCase()
                } else {
                    getLaporansByUserUseCase(userId)
                }

                val total = laporans.size
                val diproses = laporans.count { it.Status == "Diproses" || it.StatusAdmin == "Diproses" }
                val selesai = laporans.count { it.Status == "Selesai" || it.StatusAdmin == "Selesai" }
                val prioritasTinggi = laporans.count { it.TingkatUrgensi == "Tinggi" }
                
                // Sort by ID or arbitrary mock sorting for terbaru
                val terbaru = laporans.takeLast(3).reversed()

                _uiState.value = DashboardState.Success(
                    totalLaporan = total,
                    diproses = diproses,
                    selesai = selesai,
                    prioritasTinggi = prioritasTinggi,
                    laporanTerbaru = terbaru,
                    allLaporans = laporans
                )
            } catch (e: Exception) {
                _uiState.value = DashboardState.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    companion object {
        fun provideFactory(
            getLaporansUseCase: GetLaporansUseCase,
            getLaporansByUserUseCase: GetLaporansByUserUseCase,
            isAdmin: Boolean
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                    return DashboardViewModel(getLaporansUseCase, getLaporansByUserUseCase, isAdmin) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}
