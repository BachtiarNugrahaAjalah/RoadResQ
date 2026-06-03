package com.app.rrq.view.admin

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.R
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.app.rrq.model.data.Laporan
import com.app.rrq.core.injection.AppContainer
import com.app.rrq.viewmodel.admin.VerifikasiLaporanViewModel
import com.app.rrq.viewmodel.admin.VerifikasiLaporanState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifikasiLaporanScreen(
    reportId: String = "",
    onNavigate: (Int) -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: VerifikasiLaporanViewModel = viewModel(
        factory = VerifikasiLaporanViewModel.provideFactory(
            AppContainer.getLaporanByIdUseCase,
            AppContainer.getLaporansUseCase,
            AppContainer.updateStatusLaporanUseCase
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val updateStatus by viewModel.updateStatus.collectAsState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var catatanAdmin by remember { mutableStateOf("") }
    var showDialogTolak by remember { mutableStateOf(false) }
    var alasanTolak by remember { mutableStateOf("") }

    LaunchedEffect(reportId) {
        viewModel.fetchLaporanById(reportId)
    }

    LaunchedEffect(updateStatus) {
        if (updateStatus == true) {
            snackbarHostState.showSnackbar("Status laporan berhasil diperbarui!")
            viewModel.resetUpdateStatus()
        }
    }

    Scaffold(
        snackbarHost = {
            Box(modifier = Modifier.fillMaxSize()) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 50.dp)
                ) { data ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFD4EDDA)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = data.visuals.message,
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
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
                            text = "Detail & Verifikasi Laporan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is VerifikasiLaporanState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF0EA5E9))
                }
            }
            is VerifikasiLaporanState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = Color.Red)
                }
            }
            is VerifikasiLaporanState.Success -> {
                val report = state.laporan
                val statusLaporan = report.Status

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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = report.Gambar_url,
                            contentDescription = report.JudulLaporan,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
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
                                    text = report.JudulLaporan,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.Black
                                )

                                Surface(
                                    color = when (statusLaporan) {
                                        "Selesai" -> Color(0xFFD4EDDA)
                                        "Ditolak" -> Color(0xFFF8D7DA)
                                        "Diproses" -> Color(0xFFE3F2FD)
                                        else -> Color(0xFFFFF4E5)
                                    },
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = statusLaporan,
                                        color = when (statusLaporan) {
                                            "Selesai" -> Color(0xFF28A745)
                                            "Ditolak" -> Color(0xFFDC3545)
                                            "Diproses" -> Color(0xFF007BFF)
                                            else -> Color(0xFFFFA000)
                                        },
                                        modifier = Modifier.padding(
                                            horizontal = 12.dp,
                                            vertical = 4.dp
                                        ),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                Chip(report.TingkatUrgensi)
                                Spacer(modifier = Modifier.width(8.dp))
                                Chip(report.KategoriKerusakan)
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = Color(0xFFF1F3F5))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = report.Deskripsi,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFE0EDF1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Profile",
                                        tint = Color(0xFF0C7497),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(text = "PELAPOR", fontSize = 10.sp, color = Color.Gray)
                                    Text(
                                        text = "Zahra",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFE0EDF1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "Lokasi",
                                        tint = Color(0xFF0C7497),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(text = "LOKASI", fontSize = 10.sp, color = Color.Gray)
                                    Text(
                                        text = report.Lokasi,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFFE0EDF1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Tanggal",
                                        tint = Color(0xFF0C7497),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(text = "TANGGAL", fontSize = 10.sp, color = Color.Gray)
                                    Text(
                                        text = report.Tanggal,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Catatan Admin",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
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
                                fontWeight = FontWeight.Bold,
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
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (statusLaporan == "Menunggu" || statusLaporan == "Baru") {
                        Button(
                            onClick = {
                                viewModel.updateLaporanStatus(report.id, "Diverifikasi")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Verifikasi Laporan",
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(
                            onClick = { showDialogTolak = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Red)
                        ) {
                            Text(
                                text = "Tolak Laporan",
                                color = Color.Red
                            )
                        }
                    } else if (statusLaporan == "Diverifikasi" || statusLaporan == "Diproses") {
                        Text(
                            text = "Update Status:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatusOptionButton(
                                label = "Diproses",
                                isSelected = statusLaporan == "Diproses",
                                modifier = Modifier.weight(1f)
                            ) {
                                viewModel.updateLaporanStatus(report.id, "Diproses")
                            }

                            StatusOptionButton(
                                label = "Selesai",
                                isSelected = statusLaporan == "Selesai",
                                modifier = Modifier.weight(1f)
                            ) {
                                viewModel.updateLaporanStatus(report.id, "Selesai")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
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
                                    viewModel.updateLaporanStatus(report.id, "Ditolak")
                                    showDialogTolak = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text(
                                    text = "Tolak",
                                    color = Color.White
                                )
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
}

@Composable
fun StatusOptionButton(label: String, isSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) Color(0xFF007BFF) else Color.LightGray
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.Transparent
        )
    ) {
        Text(
            label,
            fontSize = 12.sp,
            color = if (isSelected) Color(0xFF007BFF) else Color.Gray
        )
    }
}

@Composable
fun Chip(text: String) {
    Surface(
        color = Color(0xFFF1F3F5),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6C757D)
        )
    }
}