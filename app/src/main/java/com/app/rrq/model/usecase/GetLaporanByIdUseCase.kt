package com.app.rrq.model.usecase

import com.app.rrq.model.data.Laporan
import com.app.rrq.model.repository.LaporanRepository

class GetLaporanByIdUseCase(private val laporanRepository: LaporanRepository) {
    suspend operator fun invoke(id: String): Laporan? {
        return laporanRepository.getLaporanById(id)
    }
}