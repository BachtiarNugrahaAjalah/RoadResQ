package com.app.rrq.ui.pages.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.SpeakerNotes
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository

@Composable
fun DetailLaporanPage(
    laporanId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var laporan by remember { mutableStateOf<Laporan?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val repository = remember { LaporanRepository() }

    LaunchedEffect(Unit) {
        repository.getLaporanById(laporanId) { result ->
            laporan = result
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF0EA5E9))
            }
        }
        laporan == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Data laporan tidak ditemukan")
            }
        }
        else -> {
            DetailLaporanContent(
                report = laporan!!,
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
    val imageByteArray = remember(report.gambarUrl) {
        try {
            if (report.gambarUrl.isNotEmpty()) {
                val base64Data = report.gambarUrl.substringAfter("base64,").trim()
                android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
            } else null
        } catch (_: Exception) { null }
    }

    Scaffold(modifier = modifier, containerColor = Color(0xFFF8F9FA)) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
                .verticalScroll(rememberScrollState()).padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                    modifier = Modifier.size(24.dp).clickable { onBack() }, tint = Color(0xFF2D3E50))
                Spacer(modifier = Modifier.width(16.dp))
                Text("Detail Laporan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D3E50))
            }
            Spacer(modifier = Modifier.height(24.dp))
            AsyncImage(
                model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                    .data(imageByteArray).crossfade(true).build(),
                contentDescription = report.judulLaporan,
                modifier = Modifier.fillMaxWidth().height(220.dp)
                    .clip(RoundedCornerShape(24.dp)).background(Color(0xFFE2E8F0)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(20.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(report.judulLaporan, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B), modifier = Modifier.weight(1f))
                        StatusBadge(status = report.status)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        UrgencyBadge(urgency = report.tingkatUrgensi)
                        CategoryBadge(category = report.kategoriKerusakan)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            InfoSectionCard(icon = Icons.Default.LocationOn, label = "LOKASI", value = report.lokasi)
            Spacer(modifier = Modifier.height(16.dp))
            InfoSectionCard(icon = Icons.Default.DateRange, label = "TANGGAL LAPOR", value = report.tanggal)
            Spacer(modifier = Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("DESKRIPSI", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(report.deskripsi, fontSize = 15.sp, color = Color(0xFF475569), lineHeight = 22.sp)
                }
            }
            if (report.statusAdmin.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBAE6FD))) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.SpeakerNotes, contentDescription = null,
                                tint = Color(0xFF0369A1), modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CATATAN ADMIN", fontSize = 11.sp, fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1), letterSpacing = 1.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(report.statusAdmin, fontSize = 15.sp, color = Color(0xFF0C4A6E),
                            fontWeight = FontWeight.Medium, lineHeight = 22.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoSectionCard(icon: ImageVector, label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(44.dp).background(Color(0xFFF0F9FF), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null,
                    modifier = Modifier.size(22.dp), tint = Color(0xFF0EA5E9))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                    color = Color(0xFF94A3B8), letterSpacing = 1.sp)
                Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (bgColor, txtColor) = when (status) {
        "Selesai" -> Color(0xFFDCFCE7) to Color(0xFF22C55E)
        "Diproses" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "Diverifikasi" -> Color(0xFFE0F2FE) to Color(0xFF0284C7)
        "Ditolak" -> Color(0xFFFEE2E2) to Color(0xFFEF4444)
        "Menunggu" -> Color(0xFFFFF7ED) to Color(0xFFF59E0B)
        else -> Color(0xFFF1F5F9) to Color(0xFF64748B)
    }
    Surface(color = bgColor, shape = RoundedCornerShape(50)) {
        Text(status, modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = txtColor)
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
    Surface(color = bgColor, shape = RoundedCornerShape(8.dp)) {
        Text(urgency, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = txtColor)
    }
}

@Composable
fun CategoryBadge(category: String) {
    Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, contentDescription = null,
                modifier = Modifier.size(12.dp), tint = Color(0xFF64748B))
            Spacer(modifier = Modifier.width(4.dp))
            Text(category, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF475569))
        }
    }
}
