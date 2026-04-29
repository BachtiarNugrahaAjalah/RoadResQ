package com.app.rrq.ui.pages.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.R
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifikasiLaporanPage(onNavigate: (Int) -> Unit = {},
                  onBack: () -> Unit = {}) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var statusLaporan by remember { mutableStateOf("Menunggu") }
    var catatanAdmin by remember { mutableStateOf("") }
    var showDialogTolak by remember { mutableStateOf(false) }
    var alasanTolak by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.Companion.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.Companion
                        .align(Alignment.Companion.TopCenter)
                        .padding(top = 50.dp)
                ) { data ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD4EDDA)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.Companion.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = data.visuals.message,
                            color = Color.Companion.Black,
                            modifier = Modifier.Companion.padding(16.dp)
                        )
                    }
                }
            }
        },
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Detail Laporan",
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 18.sp,
                            color = Color.Companion.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.Companion.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Companion.White)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.laporan1),
                    contentDescription = null,
                    contentScale = ContentScale.Companion.Crop,
                    modifier = Modifier.Companion.fillMaxWidth().height(180.dp)
                )
            }

            Spacer(modifier = Modifier.Companion.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Column(modifier = Modifier.Companion.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.Companion.fillMaxWidth(),
                        verticalAlignment = Alignment.Companion.CenterVertically
                    ) {
                        Text(
                            text = "Jalan Rusak",
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 20.sp,
                            color = Color.Companion.Black
                        )

                        Surface(
                            color = when (statusLaporan) {
                                "Selesai" -> Color(0xFFD4EDDA)
                                "Ditolak" -> Color(0xFFF8D7DA)
                                "Diproses" -> Color(0xFFE3F2FD)
                                else -> Color(0xFFFFF4E5)
                            },
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = statusLaporan,
                                color = when (statusLaporan) {
                                    "Selesai" -> Color(0xFF28A745)
                                    "Ditolak" -> Color(0xFFDC3545)
                                    "Diproses" -> Color(0xFF007BFF)
                                    else -> Color(0xFFFFA000)
                                },
                                modifier = Modifier.Companion.padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp
                                ),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Companion.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Row {
                        Chip("TINGGI")
                        Spacer(modifier = Modifier.Companion.width(8.dp))
                        Chip("Lainnya")
                    }

                    Spacer(modifier = Modifier.Companion.height(12.dp))
                    HorizontalDivider(color = Color(0xFFF1F3F5))
                    Spacer(modifier = Modifier.Companion.height(12.dp))

                    Text(
                        text = "Bisa buat berenang nih",
                        color = Color.Companion.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(12.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Column(modifier = Modifier.Companion.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Box(
                            modifier = Modifier.Companion
                                .size(50.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                                .background(Color(0xFFE0EDF1)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color(0xFF0C7497),
                                modifier = Modifier.Companion.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.Companion.width(12.dp))
                        Column(modifier = Modifier.Companion.padding(vertical = 4.dp)) {
                            Text(text = "PELAPOR", fontSize = 10.sp, color = Color.Companion.Gray)
                            Text(
                                text = "Zahra",
                                fontWeight = FontWeight.Companion.Bold,
                                fontSize = 14.sp,
                                color = Color.Companion.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Box(
                            modifier = Modifier.Companion
                                .size(50.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                                .background(Color(0xFFE0EDF1)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Lokasi",
                                tint = Color(0xFF0C7497),
                                modifier = Modifier.Companion.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.Companion.width(12.dp))

                        Column(modifier = Modifier.Companion.padding(vertical = 4.dp)) {
                            Text(text = "LOKASI", fontSize = 10.sp, color = Color.Companion.Gray)
                            Text(
                                text = "Jl. Buntu",
                                fontWeight = FontWeight.Companion.Bold,
                                fontSize = 14.sp,
                                color = Color.Companion.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                        Box(
                            modifier = Modifier.Companion
                                .size(50.dp)
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                                .background(Color(0xFFE0EDF1)),
                            contentAlignment = Alignment.Companion.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Tanggal",
                                tint = Color(0xFF0C7497),
                                modifier = Modifier.Companion.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.Companion.width(12.dp))
                        Column(modifier = Modifier.Companion.padding(vertical = 4.dp)) {
                            Text(text = "TANGGAL", fontSize = 10.sp, color = Color.Companion.Gray)
                            Text(
                                text = "23 Apr 2026, 16:25",
                                fontWeight = FontWeight.Companion.Bold,
                                fontSize = 14.sp,
                                color = Color.Companion.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))

            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Text(
                    text = "Catatan Admin",
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color.Companion.Black
                )
                TextButton(
                    onClick = {
                        if (catatanAdmin.isNotEmpty()) {
                            focusManager.clearFocus()
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Catatan berhasil disimpan!")
                            }
                        }
                    }
                ) {
                    Text(
                        "Simpan",
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color(0xFF007BFF)
                    )
                }
            }
            OutlinedTextField(
                value = catatanAdmin,
                onValueChange = { catatanAdmin = it },
                placeholder = {
                    Text(
                        text = "Tulis catatan perkembangan untuk pelapor...",
                        color = Color.Companion.Gray
                    )
                },
                modifier = Modifier.Companion.fillMaxWidth().height(100.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Companion.Black,
                    unfocusedTextColor = Color.Companion.Black
                )
            )

            Spacer(modifier = Modifier.Companion.height(20.dp))

            if (statusLaporan == "Menunggu") {
                Button(
                    onClick = {
                        statusLaporan = "Diverifikasi"
                        coroutineScope.launch { snackbarHostState.showSnackbar("Laporan diverifikasi!") }
                    },
                    modifier = Modifier.Companion.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Verifikasi Laporan",
                        color = Color.Companion.White
                    )
                }
                Spacer(modifier = Modifier.Companion.height(8.dp))
                OutlinedButton(
                    onClick = { showDialogTolak = true },
                    modifier = Modifier.Companion.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Companion.Red)
                ) {
                    Text(
                        text = "Tolak Laporan",
                        color = Color.Companion.Red
                    )
                }
            } else if (statusLaporan == "Diverifikasi" || statusLaporan == "Diproses") {
                Text(
                    text = "Update Status:",
                    fontWeight = FontWeight.Companion.Bold,
                    fontSize = 14.sp,
                    color = Color.Companion.Black
                )
                Spacer(modifier = Modifier.Companion.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusOptionButton(
                        label = "Diproses",
                        isSelected = statusLaporan == "Diproses",
                        modifier = Modifier.Companion.weight(1f)
                    ) {
                        statusLaporan = "Diproses"
                    }

                    StatusOptionButton(
                        label = "Selesai",
                        isSelected = statusLaporan == "Selesai",
                        modifier = Modifier.Companion.weight(1f)
                    ) {
                        statusLaporan = "Selesai"
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Laporan telah selesai ditangani!")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.Companion.height(30.dp))
        }
    }

    if (showDialogTolak) {
        AlertDialog(
            onDismissRequest = { showDialogTolak = false },
            title = {
                Text(
                    text = "Tolak Laporan?"
                )
            },
            text = {
                OutlinedTextField(
                    value = alasanTolak,
                    onValueChange = { alasanTolak = it },
                    placeholder = {
                        Text(
                            text = "Alasan penolakan..."
                        )
                    }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        statusLaporan = "Ditolak"
                        showDialogTolak = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Companion.Red)
                ) {
                    Text(
                        text = "Tolak",
                        color = Color.Companion.White
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialogTolak = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
fun StatusOptionButton(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) Color(0xFF007BFF) else Color.Companion.LightGray
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.Companion.Transparent
        )
    ) {
        Text(
            label,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF007BFF) else Color.Companion.Gray
        )
    }
}

@Composable
fun Chip(text: String) {
    Surface(
        color = Color(0xFFF1F3F5),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.Companion.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Companion.Bold,
            color = Color(0xFF6C757D)
        )
    }
}