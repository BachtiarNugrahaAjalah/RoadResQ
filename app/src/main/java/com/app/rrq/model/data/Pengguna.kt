package com.app.rrq.model.data

import com.google.gson.annotations.SerializedName

data class Pengguna(
    @SerializedName("id")
    val id: String,

    @SerializedName("namaLengkap")
    val namaLengkap: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("nomorTelepon")
    val nomorTelepon: String,

    @SerializedName("role")
    val role: String, // "Admin" atau "User"

    @SerializedName("status")
    val status: String // "Aktif" atau "Diblokir"
)
