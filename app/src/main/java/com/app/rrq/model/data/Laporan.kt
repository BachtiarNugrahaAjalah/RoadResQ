package com.app.rrq.model.data

data class Laporan(
    val id: String,

    val JudulLaporan: String,

    val KategoriKerusakan: String,

    val TingkatUrgensi: String,

    val Lokasi: String,

    val Deskripsi: String,

    val Gambar_url: String,

    val Status: String,

    val Tanggal: String,

    val StatusAdmin: String,

    val ReporterName: String = ""
)
