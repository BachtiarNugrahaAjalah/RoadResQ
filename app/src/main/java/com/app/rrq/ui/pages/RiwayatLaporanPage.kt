package com.app.rrq.ui.pages

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.data.LaporanData
import com.app.rrq.model.Laporan
import com.app.rrq.ui.theme.RoadResQTheme
import com.app.rrq.ui.theme.BackgroundGray

@Composable
fun RiwayatLaporanPage(
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val allReports = LaporanData.datareal
    val filteredReportsWithIndex = if (selectedFilter == "Semua") {
        allReports.mapIndexed { index, report -> index to report }
    } else {
        allReports.mapIndexed { index, report -> index to report }.filter { it.second.Status == selectedFilter }
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
                text = "${filteredReportsWithIndex.size} laporan",
                fontSize = 15.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.height(24.dp))

            FilterChipRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredReportsWithIndex) { (originalIndex, report) ->
                    LaporanCardItem(
                        report = report,
                        onClick = { onNavigateToDetail(originalIndex) }
                    )
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
    val filters = listOf("Semua", "Menunggu", "Diproses", "Selesai")
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
            Image(
                painter = painterResource(id = report.Gambar),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = report.JudulLaporan,
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
                        text = report.KategoriKerusakan,
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = report.Tanggal,
                    fontSize = 13.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            StatusBadgeItem(status = report.Status)
        }
    }
}

@Composable
fun StatusBadgeItem(status: String) {
    val backgroundColor = when (status) {
        "Selesai" -> Color(0xFFDCFCE7)
        "Diproses" -> Color(0xFFFEF3C7)
        "Menunggu" -> Color(0xFFFFF7ED)
        else -> Color(0xFFF1F5F9)
    }

    val textColor = when (status) {
        "Selesai" -> Color(0xFF22C55E)
        "Diproses" -> Color(0xFFD97706)
        "Menunggu" -> Color(0xFFF59E0B)
        else -> Color(0xFF64748B)
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RiwayatLaporanPreview() {
    RoadResQTheme {
        RiwayatLaporanPage()
    }
}
