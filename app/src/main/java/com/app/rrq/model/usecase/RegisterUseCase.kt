package com.app.rrq.model.usecase

import com.app.rrq.model.repository.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(name: String, email: String, password: String): Boolean {
        return authRepository.register(name, email, password)
    }
}
