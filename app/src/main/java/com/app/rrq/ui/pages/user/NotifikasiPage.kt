package com.app.rrq.ui.pages.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import com.app.rrq.ui.theme.TealPrimary
import com.app.rrq.ui.theme.TextPrimary
import com.app.rrq.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotifikasiPage(onBack: () -> Unit, onNavigateToDetail: (String) -> Unit = {}) {
    val repository = remember { LaporanRepository() }
    var laporanList by remember { mutableStateOf<List<Laporan>>(emptyList()) }

    LaunchedEffect(Unit) {
        repository.getSemuaLaporan { list ->
            laporanList = list.filter { 
                it.status == "Diverifikasi" || 
                it.status == "Diproses" || 
                it.status == "Selesai" || 
                it.status == "Ditolak" ||
                it.statusAdmin.isNotEmpty() 
            }.sortedByDescending { it.tanggal }
        }
    }

    Scaffold(
        containerColor = com.app.rrq.ui.theme.BackgroundGray,
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Notifikasi",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )},
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        }
    ) { padding ->
        if (laporanList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Belum ada notifikasi terbaru", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(laporanList) { laporan ->
                    NotifikasiCard(laporan, onClick = { onNavigateToDetail(laporan.id) })
                }
            }
        }
    }
}

@Composable
fun NotifikasiCard(laporan: Laporan, onClick: () -> Unit = {}) {
    val statusColor = when (laporan.status) {
        "Diverifikasi" -> Color(0xFF0284C7)
        "Selesai" -> Color(0xFF10B981)
        "Ditolak" -> Color(0xFFEF4444)
        "Diproses" -> Color(0xFFF59E0B)
        else -> TealPrimary
    }

    val notificationTitle = when (laporan.status) {
        "Diverifikasi" -> "Laporan Diverifikasi"
        "Diproses" -> "Laporan Sedang Diproses"
        "Selesai" -> "Laporan Telah Selesai"
        "Ditolak" -> "Laporan Ditolak"
        else -> "Update Laporan"
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = com.app.rrq.ui.theme.CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "$notificationTitle: ${laporan.judulLaporan}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = TextPrimary
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Status: ",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                Text(
                    text = laporan.status,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = statusColor
                )
            }
            
            if (laporan.statusAdmin.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Catatan Admin: ${laporan.statusAdmin}",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
            
            Spacer(Modifier.height(8.dp))
            Text(
                text = laporan.tanggal,
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}
