package com.app.rrq.model.data

/** Hasil kembalian dari AuthRepository.login() yang digunakan oleh LoginUseCase & LoginViewModel */
data class LoginResult(
    val userId   : String,
    val email    : String,
    val name     : String,
    val role     : String   // "Admin" | "User"
)
