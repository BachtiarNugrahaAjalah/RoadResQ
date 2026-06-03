package com.app.rrq.model.usecase

import com.app.rrq.model.repository.LaporanRepository

class UpdateStatusLaporanUseCase(private val laporanRepository: LaporanRepository) {
    suspend operator fun invoke(id: String, status: String): Boolean {
        return laporanRepository.updateStatusLaporan(id, status)
    }
}