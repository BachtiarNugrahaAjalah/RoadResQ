package com.app.rrq.model.usecase

import com.app.rrq.model.data.Laporan
import com.app.rrq.model.repository.LaporanRepository

class GetLaporansUseCase(private val repository: LaporanRepository) {
    suspend operator fun invoke(): List<Laporan> {
        return repository.getLaporans()
    }
}
