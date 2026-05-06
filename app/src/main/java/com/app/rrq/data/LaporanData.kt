package com.app.rrq.data

import com.app.rrq.R
import com.app.rrq.model.Laporan
import kotlin.collections.listOf

object LaporanData {
    val datareal = listOf(
        Laporan(
            JudulLaporan = "Jalan Berlubang di Depan Sekolah",
            KategoriKerusakan = "Jalan Berlubang",
            TingkatUrgensi = "Tinggi",
            Lokasi = "Jl. Ahmad Yani, Bandar Lampung",
            Deskripsi = "Terdapat lubang cukup besar di depan sekolah yang membahayakan pengendara, terutama saat malam hari.",
            Gambar = R.drawable.img,
            Status = "Menunggu",
            Tanggal = "23 Apr 2026"
        ),
        Laporan(
            JudulLaporan = "Aspal Retak di Area Pasar",
            KategoriKerusakan = "Aspal Retak",
            TingkatUrgensi = "Sedang",
            Lokasi = "Jl. Imam Bonjol, Pasar Tengah",
            Deskripsi = "Permukaan jalan mengalami retakan panjang yang dapat membesar jika tidak segera diperbaiki.",
            Gambar = R.drawable.img,
            Status = "Selesai",
            Tanggal = "22 Apr 2026"
        ),
        Laporan(
            JudulLaporan = "Genangan Air Merusak Jalan",
            KategoriKerusakan = "Genangan",
            TingkatUrgensi = "Tinggi",
            Lokasi = "Jl. ZA Pagar Alam, Rajabasa",
            Deskripsi = "Genangan air yang terus muncul menyebabkan permukaan jalan rusak dan licin bagi kendaraan.",
            Gambar = R.drawable.img,
            Status = "Menunggu",
            Tanggal = "21 Apr 2026"
        ),
        Laporan(
            JudulLaporan = "Jalan Amblas Dekat Perumahan",
            KategoriKerusakan = "Jalan Amblas",
            TingkatUrgensi = "Tinggi",
            Lokasi = "Perumahan Korpri, Sukarame",
            Deskripsi = "Sebagian badan jalan amblas dan cukup berbahaya untuk mobil maupun motor yang melintas.",
            Gambar = R.drawable.img,
            Status = "Diproses",
            Tanggal = "20 Apr 2026"
        ),
        Laporan(
            JudulLaporan = "Permukaan Jalan Tidak Rata",
            KategoriKerusakan = "Permukaan Tidak Rata",
            TingkatUrgensi = "Rendah",
            Lokasi = "Jl. Teuku Umar, Kedaton",
            Deskripsi = "Jalan bergelombang dan tidak rata, menyebabkan ketidaknyamanan bagi pengguna jalan.",
            Gambar = R.drawable.img,
            Status = "Selesai",
            Tanggal = "19 Apr 2026"
        )
    )

    val laporanList = listOf(
        Laporan(
            "yaya",
            "",
            "SEDANG",
            "jl.situ",
            "",
            0,
            "Ditolak",
            "24 Apr"
        ),
        Laporan(
            "ada apa ya",
            "",
            "RENDAH",
            "Jl. yuk",
            "",
            0,
            "Diverifikasi",
            "23 Apr"
        ),
        Laporan(
            "besar kepala",
            "",
            "TINGGI",
            "jl.in aja dulu",
            "",
            0,
            "Selesai",
            "23 Apr"
        ),
        Laporan(
            "Marka tidak terlihat",
            "",
            "SEDANG",
            "Jl. raya",
            "",
            0,
            "Menunggu",
            "22 Apr"
        )
    )
}