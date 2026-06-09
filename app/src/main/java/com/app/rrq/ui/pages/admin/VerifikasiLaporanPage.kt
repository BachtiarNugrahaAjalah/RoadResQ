package com.app.rrq.ui.pages.admin

import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifikasiLaporanPage(
    reportIndex: Int,
    onNavigate: (Int) -> Unit = {},
    onBack: () -> Unit = {},
    onReportsLoaded: (List<Laporan>) -> Unit = {}
) {
    var allReports by remember { mutableStateOf<List<Laporan>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val repository = remember { LaporanRepository() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        isLoading = true
        repository.getSemuaLaporan { list ->
            allReports = list
            onReportsLoaded(list)
            isLoading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    title = { Text("Verifikasi Laporan", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (reportIndex !in allReports.indices) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Laporan tidak ditemukan")
            }
        } else {
            val laporan = allReports[reportIndex]
            var catatanAdmin by remember { mutableStateOf(laporan.statusAdmin) }
            var showDialogTolak by remember { mutableStateOf(false) }
            var alasanTolak by remember { mutableStateOf("") }
            var isUpdating by remember { mutableStateOf(false) }

            val imageByteArray = remember(laporan.gambarUrl) {
                try {
                    if (laporan.gambarUrl.isNotEmpty()) {
                        val base64Data = laporan.gambarUrl.substringAfter("base64,").trim()
                        Base64.decode(base64Data, Base64.DEFAULT)
                    } else null
                } catch (e: Exception) { null }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8F9FA))
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(200.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageByteArray)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = laporan.judulLaporan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.weight(1f)
                            )
                            StatusBadgeAdmin(laporan.status)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            ChipAdmin(laporan.tingkatUrgensi)
                            Spacer(modifier = Modifier.width(8.dp))
                            ChipAdmin(laporan.kategoriKerusakan)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFF1F3F5))
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(text = laporan.deskripsi, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRowAdmin(Icons.Default.Person, "PELAPOR", "User")
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoRowAdmin(Icons.Default.LocationOn, "LOKASI", laporan.lokasi)
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoRowAdmin(Icons.Default.DateRange, "TANGGAL", laporan.tanggal)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Catatan Admin", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = catatanAdmin,
                    onValueChange = { catatanAdmin = it },
                    placeholder = { Text("Tulis catatan...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (laporan.status == "Menunggu") {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isUpdating = true
                                repository.updateStatusLaporan(laporan.id, "Diverifikasi", catatanAdmin)
                                snackbarHostState.showSnackbar("Laporan diverifikasi")
                                isUpdating = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)),
                        enabled = !isUpdating
                    ) {
                        Text("Verifikasi Laporan", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { showDialogTolak = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.Red),
                        enabled = !isUpdating
                    ) {
                        Text("Tolak Laporan", color = Color.Red)
                    }
                } else {
                    Text("Update Status:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusOptionButtonAdmin(
                            label = "Diproses",
                            isSelected = laporan.status == "Diproses",
                            modifier = Modifier.weight(1f)
                        ) {
                            coroutineScope.launch {
                                repository.updateStatusLaporan(laporan.id, "Diproses", catatanAdmin)
                            }
                        }

                        StatusOptionButtonAdmin(
                            label = "Selesai",
                            isSelected = laporan.status == "Selesai",
                            modifier = Modifier.weight(1f)
                        ) {
                            coroutineScope.launch {
                                repository.updateStatusLaporan(laporan.id, "Selesai", catatanAdmin)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }

            if (showDialogTolak) {
                AlertDialog(
                    onDismissRequest = { showDialogTolak = false },
                    title = { Text("Tolak Laporan?") },
                    text = {
                        OutlinedTextField(
                            value = alasanTolak,
                            onValueChange = { alasanTolak = it },
                            placeholder = { Text("Alasan penolakan...") }
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    repository.updateStatusLaporan(laporan.id, "Ditolak", alasanTolak)
                                    showDialogTolak = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Tolak", color = Color.White)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialogTolak = false }) { Text("Batal") }
                    }
                )
            }
        }
    }
}

@Composable
fun InfoRowAdmin(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFE0EDF1)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF0C7497), modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 10.sp, color = Color.Gray)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun StatusBadgeAdmin(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Selesai" -> Color(0xFFDCFCE7) to Color(0xFF22C55E)
        "Diproses" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "Diverifikasi" -> Color(0xFFE0F2FE) to Color(0xFF0284C7)
        "Ditolak" -> Color(0xFFFEE2E2) to Color(0xFFEF4444)
        "Menunggu" -> Color(0xFFFFF7ED) to Color(0xFFF59E0B)
        else -> Color(0xFFF1F5F9) to Color(0xFF64748B)
    }

    Surface(color = backgroundColor, shape = RoundedCornerShape(50)) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun StatusOptionButtonAdmin(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (isSelected) Color(0xFF007BFF) else Color.LightGray),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.Transparent)
    ) {
        Text(label, fontSize = 12.sp, color = if (isSelected) Color(0xFF007BFF) else Color.Gray)
    }
}

@Composable
fun ChipAdmin(text: String) {
    Surface(color = Color(0xFFF1F3F5), shape = RoundedCornerShape(16.dp)) {
        Text(text = text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6C757D))
    }
}
