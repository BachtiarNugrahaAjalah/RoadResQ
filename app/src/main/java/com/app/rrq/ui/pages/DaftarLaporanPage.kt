package com.app.rrq.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.rrq.model.Laporan
import com.app.rrq.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarLaporanPage(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Baru", "Diverifikasi", "Diproses", "Selesai")

    val laporanList = listOf(
        Laporan("yaya", "", "SEDANG", "jl.situ", "", 0, "Ditolak", "24 Apr"),
        Laporan("ada apa ya", "", "RENDAH", "Jl. yuk", "", 0, "Diverifikasi", "23 Apr"),
        Laporan("besar kepala", "", "TINGGI", "jl.in aja dulu", "", 0, "Selesai", "23 Apr"),
        Laporan("Marka tidak terlihat", "", "SEDANG", "Jl. raya", "", 0, "Menunggu", "22 Apr")
    )

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = {
            AdminBottomBar(selected = 1, onSelect = { })
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
                    text = "${laporanList.size} laporan",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Cari laporan, lokasi...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
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

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(laporanList) { laporan ->
                    LaporanCardCustom(laporan, navController)
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
fun LaporanCardCustom(laporan: Laporan, navController: NavController) {
    Card(
        onClick = {
            navController.navigate("detailLaporan")
        },
        modifier = Modifier.fillMaxWidth(),
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
                    text = laporan.JudulLaporan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Surface(
                    shape = RoundedCornerShape(50),
                    color = when (laporan.Status) {
                        "Ditolak" -> Color(0xFFFEE2E2)
                        "Diverifikasi" -> Color(0xFFE0F2FE)
                        "Selesai" -> Color(0xFFDCFCE7)
                        else -> Color(0xFFFEF3C7)
                    }
                ) {
                    Text(
                        text = laporan.Status,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (laporan.Status) {
                            "Ditolak" -> Color(0xFFEF4444)
                            "Diverifikasi" -> Color(0xFF0284C7)
                            "Selesai" -> Color(0xFF16A34A)
                            else -> Color(0xFFD97706)
                        }
                    )
                }
            }

            Text(
                text = "oleh Rapli",
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
                        text = laporan.Lokasi,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = when (laporan.TingkatUrgensi) {
                            "TINGGI" -> Color(0xFFFEE2E2)
                            "SEDANG" -> Color(0xFFFEF3C7)
                            else -> Color(0xFFE5E7EB)
                        }
                    ) {
                        Text(
                            text = laporan.TingkatUrgensi,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (laporan.TingkatUrgensi) {
                                "TINGGI" -> Color(0xFFEF4444)
                                "SEDANG" -> Color(0xFFD97706)
                                else -> Color(0xFF6B7280)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = laporan.Tanggal,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDaftarLaporanPage() {
    RoadResQTheme {
        DaftarLaporanPage(navController = rememberNavController())
    }
}
