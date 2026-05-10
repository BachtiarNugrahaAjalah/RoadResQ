package com.app.rrq.data.model

import com.google.gson.annotations.SerializedName

data class Laporan(
    @SerializedName("JudulLaporan")
    val JudulLaporan: String,

    @SerializedName("KategoriKerusakan")
    val KategoriKerusakan: String,

    @SerializedName("TingkatUrgensi")
    val TingkatUrgensi: String,

    @SerializedName("Lokasi")
    val Lokasi: String,

    @SerializedName("Deskripsi")
    val Deskripsi: String,

    @SerializedName("Gambar_url")
    val Gambar_url: String,

    @SerializedName("Status")
    val Status: String,

    @SerializedName("Tanggal")
    val Tanggal: String,

    @SerializedName("StatusAdmin")
    val StatusAdmin: String
)