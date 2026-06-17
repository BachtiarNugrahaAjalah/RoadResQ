package com.app.rrq.ui.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.ui.theme.*

data class FaqItem(
    val pertanyaan: String,
    val jawaban: String
)

data class BantuanSection(
    val judul: String,
    val icon: ImageVector,
    val warna: Color,
    val items: List<FaqItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BantuanPage(onBack: () -> Unit) {
    val sections = listOf(
        BantuanSection(
            judul = "Tentang Aplikasi",
            icon = Icons.Default.Info,
            warna = TealPrimary,
            items = listOf(
                FaqItem(
                    "Apa itu RoadResQ?",
                    "RoadResQ adalah aplikasi pelaporan kerusakan jalan yang memungkinkan masyarakat melaporkan kondisi jalan rusak secara mudah dan cepat. Laporan akan ditindaklanjuti oleh tim terkait."
                ),
                FaqItem(
                    "Siapa yang bisa menggunakan aplikasi ini?",
                    "Aplikasi ini dapat digunakan oleh siapa saja yang mendaftar sebagai pengguna. Admin memiliki akses khusus untuk mengelola dan memverifikasi laporan yang masuk."
                )
            )
        ),
        BantuanSection(
            judul = "Membuat Laporan",
            icon = Icons.Default.AddCircle,
            warna = Color(0xFF8B5CF6),
            items = listOf(
                FaqItem(
                    "Bagaimana cara membuat laporan kerusakan jalan?",
                    "1. Buka menu 'Buat Laporan' di bagian bawah aplikasi.\n2. Isi formulir laporan dengan lengkap, termasuk lokasi, deskripsi kerusakan, dan foto.\n3. Tekan tombol 'Kirim Laporan'.\n4. Laporan Anda akan diproses oleh tim admin."
                ),
                FaqItem(
                    "Apakah saya harus melampirkan foto?",
                    "Foto sangat disarankan karena membantu tim admin dalam memverifikasi kondisi kerusakan jalan secara lebih akurat dan mempercepat proses penanganan."
                ),
                FaqItem(
                    "Berapa lama laporan saya akan diproses?",
                    "Laporan akan diverifikasi oleh admin dalam 1-3 hari kerja. Status laporan dapat Anda pantau melalui menu 'Riwayat Laporan'."
                )
            )
        ),
        BantuanSection(
            judul = "Status & Riwayat Laporan",
            icon = Icons.Default.History,
            warna = Color(0xFF10B981),
            items = listOf(
                FaqItem(
                    "Apa arti status laporan saya?",
                    "• MENUNGGU: Laporan sedang menunggu verifikasi admin.\n• DIPROSES: Laporan telah diverifikasi dan sedang dalam penanganan.\n• SELESAI: Kerusakan jalan telah diperbaiki.\n• DITOLAK: Laporan tidak memenuhi syarat verifikasi."
                ),
                FaqItem(
                    "Bagaimana cara melihat riwayat laporan saya?",
                    "Buka tab 'Riwayat' di menu bawah aplikasi. Di sana Anda bisa melihat semua laporan yang pernah Anda buat beserta status terkini masing-masing laporan."
                ),
                FaqItem(
                    "Apakah saya bisa mengedit laporan yang sudah dikirim?",
                    "Laporan yang sudah dikirim tidak dapat diedit untuk menjaga integritas data. Jika terdapat kesalahan, Anda dapat membuat laporan baru dengan informasi yang benar."
                )
            )
        ),
        BantuanSection(
            judul = "Akun & Profil",
            icon = Icons.Default.Person,
            warna = Color(0xFFF59E0B),
            items = listOf(
                FaqItem(
                    "Bagaimana cara mengganti kata sandi?",
                    "Buka menu Profil → Privasi & Keamanan → Ubah Kata Sandi. Masukkan kata sandi lama Anda, lalu masukkan kata sandi baru minimal 6 karakter."
                ),
                FaqItem(
                    "Bagaimana cara mengganti foto profil?",
                    "Buka menu Profil → Edit Profil, lalu ketuk foto profil atau ikon kamera. Pilih 'Pilih dari Galeri' untuk mengunggah foto baru dari perangkat Anda."
                ),
                FaqItem(
                    "Bagaimana cara mengganti email akun?",
                    "Buka Profil → Edit Profil, ubah kolom email, lalu simpan. Anda akan diminta memasukkan kata sandi untuk konfirmasi keamanan. Firebase akan mengirim email verifikasi ke alamat email baru Anda."
                )
            )
        ),
        BantuanSection(
            judul = "Notifikasi",
            icon = Icons.Default.Notifications,
            warna = Color(0xFFEF4444),
            items = listOf(
                FaqItem(
                    "Kenapa saya tidak mendapatkan notifikasi?",
                    "Pastikan notifikasi untuk aplikasi RoadResQ diaktifkan di pengaturan perangkat Anda. Buka Pengaturan → Aplikasi → RoadResQ → Notifikasi, lalu aktifkan."
                ),
                FaqItem(
                    "Apa saja jenis notifikasi yang akan saya terima?",
                    "Anda akan menerima notifikasi ketika:\n• Status laporan berubah (diproses, selesai, ditolak)\n• Ada pengumuman penting dari admin\n• Laporan Anda telah diverifikasi"
                )
            )
        )
    )

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Pusat Bantuan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = TealPrimary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(TealPrimary, TealDark))
                    )
                    .padding(vertical = 24.dp, horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpCenter,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Bagaimana kami bisa membantu?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        "Temukan jawaban atas pertanyaan Anda di bawah ini",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── FAQ Sections ──────────────────────────────────────
            sections.forEach { section ->
                BantuanSectionCard(
                    section = section,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Kontak Dukungan ───────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(TealLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContactSupport,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Hubungi Kami",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    KontakItem(
                        icon = Icons.Default.Email,
                        label = "Email Dukungan",
                        value = "support@roadresq.id"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    KontakItem(
                        icon = Icons.Default.Phone,
                        label = "Telepon",
                        value = "0800-1234-5678 (Gratis)"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    KontakItem(
                        icon = Icons.Default.Schedule,
                        label = "Jam Operasional",
                        value = "Senin – Jumat, 08.00 – 17.00 WIB"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Footer ────────────────────────────────────────────
            Text(
                text = "RoadResQ v1.0.0 · © 2025",
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun BantuanSectionCard(
    section: BantuanSection,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val expandedItems = remember { mutableStateMapOf<Int, Boolean>() }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Section header – bisa di-tap untuk expand/collapse semua
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(section.warna.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = section.icon,
                        contentDescription = null,
                        tint = section.warna,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = section.judul,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "arrow")
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp).rotate(rotation)
                )
            }

            // FAQ Items
            AnimatedVisibility(visible = expanded) {
                Column {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = Color(0xFFE5E7EB)
                    )
                    section.items.forEachIndexed { index, faq ->
                        FaqItemRow(
                            faq = faq,
                            isExpanded = expandedItems[index] == true,
                            onToggle = {
                                expandedItems[index] = expandedItems[index] != true
                            }
                        )
                        if (index < section.items.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 0.5.dp,
                                color = Color(0xFFE5E7EB)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqItemRow(
    faq: FaqItem,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val rotation by animateFloatAsState(if (isExpanded) 180f else 0f, label = "arrow")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faq.pertanyaan,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = TextPrimary,
                modifier = Modifier.weight(1f),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp).rotate(rotation)
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundGray)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = faq.jawaban,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun KontakItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(TealLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = TealPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
            Text(value, fontSize = 14.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
        }
    }
}
