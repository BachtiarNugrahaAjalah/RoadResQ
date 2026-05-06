package com.app.rrq.model

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val date: String,
    val role: Role = Role.USER,
    val status: Status = Status.AKTIF
)