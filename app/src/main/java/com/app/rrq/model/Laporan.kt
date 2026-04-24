package com.app.rrq.model

import androidx.annotation.DrawableRes

data class Laporan(
    val JudulLaporan: String,
    val KategoriKerusakan: String,
    val TingkatUrgensi: String,
    val Lokasi: String,
    val Deskripsi: String,
    @DrawableRes val Gambar: Int,
    val Status: String,
    val Tanggal: String
)