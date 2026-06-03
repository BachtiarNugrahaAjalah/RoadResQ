package com.app.rrq.model.usecase

import com.app.rrq.model.repository.PenggunaRepository

class UpdateStatusPenggunaUseCase(private val penggunaRepository: PenggunaRepository) {
    suspend operator fun invoke(id: String, status: String): Boolean {
        return penggunaRepository.updateStatusPengguna(id, status)
    }
}