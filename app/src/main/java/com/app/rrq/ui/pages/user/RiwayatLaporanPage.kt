package com.app.rrq.ui.pages.user

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.app.rrq.data.model.Laporan
import com.app.rrq.model.LaporanViewModel
import com.app.rrq.ui.pages.UserBottomBar
import com.app.rrq.ui.theme.BackgroundGray

@Composable
fun RiwayatLaporanPage(
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {},
    viewModel: LaporanViewModel = viewModel()
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val allReports = viewModel.laporanList
    val isLoading = viewModel.isFetching

    LaunchedEffect(Unit) {
        viewModel.muatSemuaLaporan()
    }

    val filteredReports = if (selectedFilter == "Semua") {
        allReports
    } else {
        allReports.filter { it.status == selectedFilter }
    }

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundGray,
        bottomBar = {
            UserBottomBar(selected = 2, onSelect = onNavigate)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Riwayat Laporan",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D3E50)
            )
            Text(
                text = "${filteredReports.size} laporan",
                fontSize = 15.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(24.dp))

            FilterChipRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF088395))
                }
            } else if (allReports.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada riwayat laporan", color = Color(0xFF64748B))
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredReports) { report ->
                        LaporanCardItem(
                            report = report,
                            onClick = { onNavigateToDetail(report.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipRow(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("Semua", "Menunggu", "Diverifikasi", "Diproses", "Selesai", "Ditolak")
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(filters) { filter ->
            val isSelected = filter == selectedFilter
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(30))
                    .clickable { onFilterSelected(filter) },
                color = if (isSelected) Color(0xFF088395) else Color(0xFFF1F5F9),
            ) {
                Text(
                    text = filter,
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp),
                    color = if (isSelected) Color.White else Color(0xFF64748B),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LaporanCardItem(report: Laporan, onClick: () -> Unit) {
    val bitmap = remember(report.gambarUrl) {
        if (report.gambarUrl.isEmpty()) return@remember null
        try {
            val base64Data = report.gambarUrl.substringAfter("base64,").trim()
            val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (_: Exception) {
            null
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = bitmap,
                contentDescription = report.judulLaporan,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE2E8F0)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = report.judulLaporan,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = report.kategoriKerusakan,
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = report.tanggal,
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            StatusBadgeItem(status = report.status)
        }
    }
}

@Composable
fun StatusBadgeItem(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Selesai" -> Color(0xFFDCFCE7) to Color(0xFF166534)
        "Diproses" -> Color(0xFFDBEAFE) to Color(0xFF1E40AF)
        "Diverifikasi" -> Color(0xFFE0F2FE) to Color(0xFF075985)
        "Ditolak" -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
        "Menunggu" -> Color(0xFFFEF3C7) to Color(0xFF92400E)
        else -> Color(0xFFF1F5F9) to Color(0xFF64748B)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
