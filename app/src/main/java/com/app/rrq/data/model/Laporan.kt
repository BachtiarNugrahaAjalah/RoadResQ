package com.app.rrq.data.model

import com.google.firebase.firestore.PropertyName

data class Laporan(
    @get:PropertyName("id") @set:PropertyName("id") @PropertyName("id")
    var id: String = "",

    @get:PropertyName("JudulLaporan") @set:PropertyName("JudulLaporan") @PropertyName("JudulLaporan")
    var judulLaporan: String = "",

    @get:PropertyName("KategoriKerusakan") @set:PropertyName("KategoriKerusakan") @PropertyName("KategoriKerusakan")
    var kategoriKerusakan: String = "",

    @get:PropertyName("TingkatUrgensi") @set:PropertyName("TingkatUrgensi") @PropertyName("TingkatUrgensi")
    var tingkatUrgensi: String = "Sedang",

    @get:PropertyName("Lokasi") @set:PropertyName("Lokasi") @PropertyName("Lokasi")
    var lokasi: String = "",

    @get:PropertyName("Deskripsi") @set:PropertyName("Deskripsi") @PropertyName("Deskripsi")
    var deskripsi: String = "",

    @get:PropertyName("Gambar_url") @set:PropertyName("Gambar_url") @PropertyName("Gambar_url")
    var gambarUrl: String = "",

    @get:PropertyName("Status") @set:PropertyName("Status") @PropertyName("Status")
    var status: String = "Menunggu",

    @get:PropertyName("Tanggal") @set:PropertyName("Tanggal") @PropertyName("Tanggal")
    var tanggal: String = "",

    @get:PropertyName("StatusAdmin") @set:PropertyName("StatusAdmin") @PropertyName("StatusAdmin")
    var statusAdmin: String = ""
)