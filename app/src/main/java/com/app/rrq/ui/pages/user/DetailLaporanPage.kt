package com.app.rrq.ui.pages.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import com.app.rrq.ui.theme.RoadResQTheme

@Composable
fun DetailLaporanPage(
    reportIndex: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    onReportsLoaded: (List<Laporan>) -> Unit = {}
) {
    var allReports by remember { mutableStateOf<List<Laporan>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val repository = remember { LaporanRepository() }

    LaunchedEffect(Unit) {
        try {
            allReports = repository.getLaporans()
            onReportsLoaded(allReports)
        } catch (e: Exception) {
            errorMessage = e.message
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF0EA5E9))
            }
        }
        errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Terjadi kesalahan: $errorMessage")
            }
        }
        reportIndex !in allReports.indices -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data laporan tidak ditemukan")
            }
        }
        else -> {
            DetailLaporanContent(
                report = allReports[reportIndex],
                onBack = onBack,
                modifier = modifier
            )
        }
    }
}

@Composable
fun DetailLaporanContent(
    report: Laporan,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = Color(0xFF2D3E50)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Detail Laporan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3E50)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Image Section
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(report.Gambar_url)
                    .crossfade(true)
                    .build(),
                contentDescription = report.JudulLaporan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFE2E8F0)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Title and Status Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = report.JudulLaporan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.weight(1f)
                        )
                        StatusBadge(status = report.Status)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        UrgencyBadge(urgency = report.TingkatUrgensi)
                        CategoryBadge(category = report.KategoriKerusakan)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Location and Date info
            InfoSectionCard(
                icon = Icons.Default.LocationOn,
                label = "LOKASI",
                value = report.Lokasi
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoSectionCard(
                icon = Icons.Default.DateRange,
                label = "TANGGAL LAPOR",
                value = report.Tanggal
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "DESKRIPSI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = report.Deskripsi,
                        fontSize = 15.sp,
                        color = Color(0xFF475569),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoSectionCard(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFF0F9FF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = Color(0xFF0EA5E9)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp
                )
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E293B)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, txtColor) = when (status) {
        "Selesai" -> Color(0xFFDCFCE7) to Color(0xFF22C55E)
        "Diproses" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "Menunggu" -> Color(0xFFFFF7ED) to Color(0xFFF59E0B)
        else -> Color(0xFFF1F5F9) to Color(0xFF64748B)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = txtColor
        )
    }
}

@Composable
fun UrgencyBadge(urgency: String) {
    val (bgColor, txtColor) = when (urgency) {
        "Tinggi" -> Color(0xFFFEE2E2) to Color(0xFFEF4444)
        "Sedang" -> Color(0xFFFEF3C7) to Color(0xFFF59E0B)
        "Rendah" -> Color(0xFFDCFCE7) to Color(0xFF22C55E)
        else -> Color(0xFFF1F5F9) to Color(0xFF64748B)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = urgency,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = txtColor
        )
    }
}

@Composable
fun CategoryBadge(category: String) {
    Surface(
        color = Color(0xFFF1F5F9),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = category,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailLaporanPreview() {
    val mockReport = Laporan(
        JudulLaporan = "Jalan Berlubang",
        KategoriKerusakan = "Infrastruktur",
        TingkatUrgensi = "Tinggi",
        Lokasi = "Jakarta",
        Deskripsi = "Deskripsi laporan...",
        Gambar_url = "",
        Status = "Diproses",
        Tanggal = "2025-05-20",
        StatusAdmin = "Verified"
    )
    RoadResQTheme {
        DetailLaporanContent(report = mockReport, onBack = {})
    }
}
