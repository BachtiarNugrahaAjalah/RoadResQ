package com.app.rrq.model.data

data class Pengguna(
    val id: String,

    val namaLengkap: String,

    val email: String,

    val nomorTelepon: String,

    val role: String, // "Admin" atau "User"

    val status: String, // "Aktif" atau "Diblokir"

    val tanggalDaftar: String = ""
)
