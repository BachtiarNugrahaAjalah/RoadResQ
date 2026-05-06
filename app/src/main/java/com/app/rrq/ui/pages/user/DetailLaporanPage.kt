package com.app.rrq.ui.pages.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.data.LaporanData
import com.app.rrq.model.Laporan
import com.app.rrq.ui.theme.RoadResQTheme

@Composable
fun DetailLaporanPage(
    reportIndex: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    val report = LaporanData.datareal.getOrNull(reportIndex) ?: LaporanData.datareal[0]

    DetailLaporanContent(
        report = report,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun DetailLaporanContent(
    report: Laporan,
    onBack: () -> Unit,
    modifier: Modifier = Modifier.Companion
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.Companion.height(24.dp))

            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.Companion
                        .size(24.dp)
                        .clickable { onBack() },
                    tint = Color(0xFF2D3E50)
                )
                Spacer(modifier = Modifier.Companion.width(16.dp))
                Text(
                    text = "Detail Laporan",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF2D3E50)
                )
            }

            Spacer(modifier = Modifier.Companion.height(24.dp))

            Image(
                painter = painterResource(id = report.Gambar),
                contentDescription = null,
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentScale = ContentScale.Companion.Crop
            )

            Spacer(modifier = Modifier.Companion.height(20.dp))

            Card(
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.Companion.padding(20.dp)) {
                    Row(
                        modifier = Modifier.Companion.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        Text(
                            text = report.JudulLaporan,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Companion.Bold,
                            color = Color(0xFF1E293B),
                            modifier = Modifier.Companion.weight(1f)
                        )
                        StatusBadge(status = report.Status)
                    }

                    Spacer(modifier = Modifier.Companion.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        UrgencyBadge(urgency = report.TingkatUrgensi)
                        CategoryBadge(category = report.KategoriKerusakan)
                    }
                }
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            InfoSectionCard(
                icon = Icons.Default.LocationOn,
                label = "LOKASI",
                value = report.Lokasi
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))

            InfoSectionCard(
                icon = Icons.Default.DateRange,
                label = "TANGGAL LAPOR",
                value = report.Tanggal
            )

            Spacer(modifier = Modifier.Companion.height(16.dp))

            Card(
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
            ) {
                Column(modifier = Modifier.Companion.padding(20.dp)) {
                    Text(
                        text = "DESKRIPSI",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color(0xFF94A3B8),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.Companion.height(8.dp))
                    Text(
                        text = report.Deskripsi,
                        fontSize = 15.sp,
                        color = Color(0xFF475569),
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(32.dp))
        }
    }
}

@Composable
fun InfoSectionCard(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier.Companion.padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Box(
                modifier = Modifier.Companion
                    .size(44.dp)
                    .background(
                        Color(0xFFF0F9FF),
                        androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Companion.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.Companion.size(22.dp),
                    tint = Color(0xFF0EA5E9)
                )
            }
            Spacer(modifier = Modifier.Companion.width(16.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 1.sp
                )
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Companion.Medium,
                    color = Color(0xFF1E293B)
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: String) {
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
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Text(
            text = urgency,
            modifier = Modifier.Companion.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Companion.ExtraBold,
            color = txtColor
        )
    }
}

@Composable
fun CategoryBadge(category: String) {
    Surface(
        color = Color(0xFFF1F5F9),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.Companion.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.Companion.size(12.dp),
                tint = Color(0xFF64748B)
            )
            Spacer(modifier = Modifier.Companion.width(4.dp))
            Text(
                text = category,
                fontSize = 11.sp,
                fontWeight = FontWeight.Companion.Bold,
                color = Color(0xFF475569)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailLaporanPreview() {
    RoadResQTheme {
        DetailLaporanPage(reportIndex = 0, onBack = {})
    }
}