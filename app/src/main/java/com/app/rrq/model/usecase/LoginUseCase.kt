package com.app.rrq.model.usecase

import com.app.rrq.model.data.LoginResult
import com.app.rrq.model.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        return authRepository.login(email, password)
    }
}
