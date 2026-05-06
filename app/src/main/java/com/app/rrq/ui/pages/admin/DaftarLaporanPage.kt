package com.app.rrq.ui.pages.admin

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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.app.rrq.ui.pages.AdminBottomBar
import com.app.rrq.ui.theme.BackgroundGray
import com.app.rrq.ui.theme.RoadResQTheme
import com.app.rrq.ui.theme.TealPrimary
import com.app.rrq.ui.theme.TextPrimary
import com.app.rrq.ui.theme.TextSecondary
import com.app.rrq.data.LaporanData.laporanList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DaftarLaporanPage(navController: NavController) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Baru", "Diverifikasi", "Diproses", "Selesai")

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = {
            AdminBottomBar(selected = 1, onSelect = { })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.Companion.padding(horizontal = 24.dp, vertical = 24.dp)) {
                Text(
                    text = "Daftar Laporan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Companion.Bold,
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
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedBorderColor = Color.Companion.Transparent,
                    unfocusedBorderColor = Color.Companion.Transparent,
                )
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))

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

            Spacer(modifier = Modifier.Companion.height(16.dp))

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
        modifier = Modifier.Companion.clickable { onClick() },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
        color = if (isSelected) TealPrimary else Color(0xFFE5E7EB)
    ) {
        Text(
            text = label,
            modifier = Modifier.Companion.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Companion.Medium,
            color = if (isSelected) Color.Companion.White else TextPrimary
        )
    }
}

@Composable
fun LaporanCardCustom(laporan: Laporan, navController: NavController) {
    Card(
        onClick = {
            navController.navigate("detailLaporan")
        },
        modifier = Modifier.Companion.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.Companion.padding(20.dp)) {
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.Top
            ) {
                Text(
                    text = laporan.JudulLaporan,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = TextPrimary,
                    modifier = Modifier.Companion.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Companion.Ellipsis
                )

                Surface(
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                    color = when (laporan.Status) {
                        "Ditolak" -> Color(0xFFFEE2E2)
                        "Diverifikasi" -> Color(0xFFE0F2FE)
                        "Selesai" -> Color(0xFFDCFCE7)
                        else -> Color(0xFFFEF3C7)
                    }
                ) {
                    Text(
                        text = laporan.Status,
                        modifier = Modifier.Companion.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Companion.SemiBold,
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
                modifier = Modifier.Companion.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.Companion.weight(1f),
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.Companion.size(16.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(4.dp))
                    Text(
                        text = laporan.Lokasi,
                        fontSize = 13.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Companion.Ellipsis
                    )
                }

                Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                    Surface(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                        color = when (laporan.TingkatUrgensi) {
                            "TINGGI" -> Color(0xFFFEE2E2)
                            "SEDANG" -> Color(0xFFFEF3C7)
                            else -> Color(0xFFE5E7EB)
                        }
                    ) {
                        Text(
                            text = laporan.TingkatUrgensi,
                            modifier = Modifier.Companion.padding(
                                horizontal = 8.dp,
                                vertical = 2.dp
                            ),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Companion.Bold,
                            color = when (laporan.TingkatUrgensi) {
                                "TINGGI" -> Color(0xFFEF4444)
                                "SEDANG" -> Color(0xFFD97706)
                                else -> Color(0xFF6B7280)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.Companion.width(8.dp))
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