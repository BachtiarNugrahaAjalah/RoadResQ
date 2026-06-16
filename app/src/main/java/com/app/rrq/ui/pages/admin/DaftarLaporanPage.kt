package com.app.rrq.ui.pages.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import com.app.rrq.ui.pages.AdminBottomBar
import com.app.rrq.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarLaporanPage(
    onNavigate: (Int) -> Unit = {},
    onNavigateToVerifikasi: (String) -> Unit = {},
    onReportsLoaded: (List<Laporan>) -> Unit = {}
) {
    var allReports by remember { mutableStateOf<List<Laporan>>(emptyList()) }
    val repository = remember { LaporanRepository() }

    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Menunggu", "Diverifikasi", "Diproses", "Selesai", "Ditolak")

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        repository.getSemuaLaporanAdmin { list ->
            allReports = list
            onReportsLoaded(list)
            isLoading = false
        }
    }

    val filteredReports = if (selectedFilter == "Semua") {
        allReports
    } else {
        allReports.filter { it.status == selectedFilter }
    }

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = {
            AdminBottomBar(selected = 1, onSelect = onNavigate)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp)) {
                Text(
                    text = "Daftar Laporan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${filteredReports.size} laporan",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Cari laporan, lokasi...", color = TextSecondary) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChipCustom(
                        label = filter,
                        isSelected = selectedFilter == filter,
                        onClick = { selectedFilter = filter }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TealPrimary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(filteredReports) { index, laporan ->
                        LaporanCardCustom(laporan, onClick = { onNavigateToVerifikasi(laporan.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipCustom(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(50),
        color = if (isSelected) TealPrimary else Color(0xFFE5E7EB)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color.White else TextPrimary
        )
    }
}

@Composable
fun LaporanCardCustom(laporan: Laporan, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = laporan.judulLaporan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val (bgColor, txtColor) = when (laporan.status) {
                    "Selesai" -> Color(0xFFDCFCE7) to Color(0xFF22C55E)
                    "Diproses" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
                    "Diverifikasi" -> Color(0xFFE0F2FE) to Color(0xFF0284C7)
                    "Ditolak" -> Color(0xFFFEE2E2) to Color(0xFFEF4444)
                    "Menunggu" -> Color(0xFFFFF7ED) to Color(0xFFF59E0B)
                    else -> Color(0xFFF1F5F9) to Color(0xFF64748B)
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = bgColor
                ) {
                    Text(
                        text = laporan.status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = txtColor
                    )
                }
            }

            Text(
                text = "oleh User",
                fontSize = 14.sp,
                color = TextSecondary,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = laporan.lokasi,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = when (laporan.tingkatUrgensi) {
                            "Tinggi" -> Color(0xFFFEE2E2)
                            "Sedang" -> Color(0xFFFEF3C7)
                            else -> Color(0xFFE5E7EB)
                        }
                    ) {
                        Text(
                            text = laporan.tingkatUrgensi,
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 2.dp
                            ),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (laporan.tingkatUrgensi) {
                                "Tinggi" -> Color(0xFFEF4444)
                                "Sedang" -> Color(0xFFD97706)
                                else -> Color(0xFF6B7280)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = laporan.tanggal,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
