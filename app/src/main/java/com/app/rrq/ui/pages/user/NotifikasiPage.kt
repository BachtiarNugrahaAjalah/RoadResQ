package com.app.rrq.ui.pages.user

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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

    // Listen realtime — kalau status berubah, langsung update
    LaunchedEffect(Unit) {
        repository.getSemuaLaporan { list ->
            // Tampilkan laporan yang sudah diproses/diupdate oleh admin
            // (status berubah dari "Menunggu" ATAU sudah ada catatan admin)
            laporanList = list.filter { it.status != "Menunggu" || it.statusAdmin.isNotEmpty() }
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
                Text("Belum ada notifikasi", color = MaterialTheme.colorScheme.onSurfaceVariant)
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
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = com.app.rrq.ui.theme.CardWhite),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Update Laporan: ${laporan.judulLaporan}",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Status: ${laporan.status}",
                fontSize = 13.sp,
                color = TealPrimary
            )
            if (laporan.statusAdmin.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Catatan Admin: ${laporan.statusAdmin}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = laporan.tanggal,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}