package com.app.rrq.ui.pages.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.app.rrq.ui.pages.UserBottomBar
import com.app.rrq.ui.theme.BackgroundGray
import com.app.rrq.ui.theme.RoadResQTheme

@Composable
fun RiwayatLaporanPage(
    modifier: Modifier = Modifier.Companion,
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
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.Companion.height(32.dp))

            Text(
                text = "Riwayat Laporan",
                fontSize = 26.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color(0xFF2D3E50)
            )
            Text(
                text = "${filteredReportsWithIndex.size} laporan",
                fontSize = 15.sp,
                color = Color(0xFF94A3B8)
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            FilterChipRow(
                selectedFilter = selectedFilter,
                onFilterSelected = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

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
                modifier = Modifier.Companion
                    .clip(RoundedCornerShape(30))
                    .clickable { onFilterSelected(filter) },
                color = if (isSelected) Color(0xFF088395) else Color(0xFFF1F5F9),
            ) {
                Text(
                    text = filter,
                    modifier = Modifier.Companion.padding(horizontal = 22.dp, vertical = 10.dp),
                    color = if (isSelected) Color.Companion.White else Color(0xFF64748B),
                    fontWeight = if (isSelected) FontWeight.Companion.Bold else FontWeight.Companion.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LaporanCardItem(report: Laporan, onClick: () -> Unit) {
    Card(
        modifier = Modifier.Companion
            .fillMaxWidth()
            .clickable { onClick() },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.Companion
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Image(
                painter = painterResource(id = report.Gambar),
                contentDescription = null,
                modifier = Modifier.Companion
                    .size(80.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Companion.Crop
            )

            Spacer(modifier = Modifier.Companion.width(16.dp))

            Column(
                modifier = Modifier.Companion.weight(1f)
            ) {
                Text(
                    text = report.JudulLaporan,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF1E293B),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.Companion.height(4.dp))

                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.Companion.size(14.dp),
                        tint = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.Companion.width(6.dp))
                    Text(
                        text = report.KategoriKerusakan,
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.Companion.height(12.dp))

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
        shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
    ) {
        Text(
            text = status,
            modifier = Modifier.Companion.padding(horizontal = 14.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Companion.Bold,
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