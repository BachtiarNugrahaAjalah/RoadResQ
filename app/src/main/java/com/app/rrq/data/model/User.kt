package com.app.rrq.data.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val date: String = "",
    val role: Role = Role.USER,
    val status: Status = Status.AKTIF
)