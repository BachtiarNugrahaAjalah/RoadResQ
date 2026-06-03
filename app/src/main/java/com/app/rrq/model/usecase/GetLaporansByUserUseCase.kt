package com.app.rrq.model.usecase

import com.app.rrq.model.data.Laporan
import com.app.rrq.model.repository.LaporanRepository

class GetLaporansByUserUseCase(private val laporanRepository: LaporanRepository) {
    suspend operator fun invoke(userId: String): List<Laporan> {
        return laporanRepository.getLaporansByUser(userId)
    }
}