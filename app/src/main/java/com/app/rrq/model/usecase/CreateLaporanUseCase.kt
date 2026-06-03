package com.app.rrq.model.usecase

import com.app.rrq.model.repository.LaporanRepository

class CreateLaporanUseCase(private val laporanRepository: LaporanRepository) {
    suspend operator fun invoke(
        judul: String,
        kategori: String,
        urgensi: String,
        lokasi: String,
        deskripsi: String
    ): Boolean {
        return laporanRepository.addLaporan(judul, kategori, urgensi, lokasi, deskripsi)
    }
}
