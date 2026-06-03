package com.app.rrq.model.usecase

import com.app.rrq.model.data.Pengguna
import com.app.rrq.model.repository.PenggunaRepository

class GetPenggunasUseCase(private val penggunaRepository: PenggunaRepository) {
    suspend operator fun invoke(): List<Pengguna> {
        return penggunaRepository.getPenggunas()
    }
}