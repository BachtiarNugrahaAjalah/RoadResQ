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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import com.app.rrq.ui.theme.TextPrimary
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifikasiLaporanPage(
    laporanId: String,
    onNavigate: (Int) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var laporan by remember { mutableStateOf<Laporan?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val repository = remember { LaporanRepository() }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        repository.getLaporanById(laporanId) { result ->
            laporan = result
            isLoading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Surface(shadowElevation = 4.dp) {
                TopAppBar(
                    title = { Text("Detail Laporan",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary) },
                    navigationIcon = {
                        IconButton(onClick = { onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
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
        } else if (laporan == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Laporan tidak ditemukan")
            }
        } else {
            val data = laporan!!
            var catatanAdmin by remember { mutableStateOf(data.statusAdmin) }
            var showDialogTolak by remember { mutableStateOf(false) }
            var alasanTolak by remember { mutableStateOf("") }
            var isUpdating by remember { mutableStateOf(false) }
            var isSavingCatatan by remember { mutableStateOf(false) }

            // Logika: Tampilkan catatan HANYA jika sudah diverifikasi (Diverifikasi, Diproses, Selesai)
            val isVerified = data.status != "Menunggu" && data.status != "Ditolak"
            val canEditNote = data.status == "Diverifikasi" || data.status == "Diproses"

            val imageByteArray = remember(data.gambarUrl) {
                try {
                    if (data.gambarUrl.isNotEmpty()) {
                        val base64Data = data.gambarUrl.substringAfter("base64,").trim()
                        Base64.decode(base64Data, Base64.DEFAULT)
                    } else null
                } catch (e: Exception) { null }
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
                    .background(Color(0xFFF8F9FA)).verticalScroll(rememberScrollState()).padding(16.dp)
            ) {
                Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(imageByteArray).crossfade(true).build(),
                        contentDescription = null, contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(data.judulLaporan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color(0xFF1E293B),
                                modifier = Modifier.weight(1f))
                            StatusBadgeAdmin(data.status)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            UrgensiBadgeAdmin(data.tingkatUrgensi)
                            Spacer(modifier = Modifier.width(8.dp))
                            ChipAdmin(data.kategoriKerusakan)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFF1F3F5))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = data.deskripsi, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRowAdmin(Icons.Default.LocationOn, "LOKASI", data.lokasi)
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoRowAdmin(Icons.Default.DateRange, "TANGGAL", data.tanggal)
                    }
                }
                
                // --- BAGIAN CATATAN ADMIN (Hanya tampil jika sudah diverifikasi) ---
                if (isVerified) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Catatan Admin",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        if (canEditNote) {
                            TextButton(
                                onClick = {
                                    focusManager.clearFocus()
                                    coroutineScope.launch {
                                        isSavingCatatan = true
                                        repository.updateStatusLaporan(data.id, data.status, catatanAdmin)
                                        snackbarHostState.showSnackbar("Catatan berhasil disimpan")
                                        isSavingCatatan = false
                                    }
                                },
                                enabled = !isSavingCatatan,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                            ) {
                                if (isSavingCatatan) {
                                    CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                                } else {
                                    Text("Simpan", color = Color(0xFF0C7497), fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = catatanAdmin,
                        onValueChange = { catatanAdmin = it },
                        enabled = canEditNote,
                        placeholder = { Text("Tulis catatan perkembangan...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0C7497),
                            unfocusedBorderColor = if (catatanAdmin.isNotEmpty()) Color(0xFF0C7497).copy(alpha = 0.5f) else Color(0xFFD1D5DB),
                            disabledBorderColor = Color(0xFFE2E8F0),
                            disabledTextColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Logika Tombol Aksi Status
                when (data.status) {
                    "Menunggu" -> {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isUpdating = true
                                    repository.updateStatusLaporan(data.id, "Diverifikasi", "")
                                    laporan = data.copy(status = "Diverifikasi")
                                    snackbarHostState.showSnackbar("Laporan diverifikasi")
                                    isUpdating = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF28A745)),
                            enabled = !isUpdating
                        ) { Text("Verifikasi Laporan", color = Color.White) }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedButton(onClick = { showDialogTolak = true },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Color.Red), enabled = !isUpdating) {
                            Text("Tolak Laporan", color = Color.Red)
                        }
                    }
                    "Diverifikasi", "Diproses" -> {
                        Text("Update Status:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            StatusOptionButtonAdmin(
                                "Diproses",
                                data.status == "Diproses",
                                Modifier.weight(1f),
                                selectedColor = Color(0xFF1E40AF)
                            ) {
                                if (data.status != "Diproses") {
                                    coroutineScope.launch { 
                                        repository.updateStatusLaporan(data.id, "Diproses", catatanAdmin)
                                        laporan = data.copy(status = "Diproses", statusAdmin = catatanAdmin)
                                    }
                                }
                            }
                            StatusOptionButtonAdmin(
                                "Selesai",
                                data.status == "Selesai",
                                Modifier.weight(1f),
                                selectedColor = Color(0xFF166534)
                            ) {
                                coroutineScope.launch { 
                                    repository.updateStatusLaporan(data.id, "Selesai", catatanAdmin)
                                    laporan = data.copy(status = "Selesai", statusAdmin = catatanAdmin)
                                }
                            }
                        }
                    }
                    "Selesai", "Ditolak" -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                                Text(
                                    text = "Laporan ini sudah berstatus ${data.status.uppercase()}.",
                                    modifier = Modifier.fillMaxWidth(),
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                if (data.status == "Ditolak" && data.statusAdmin.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Alasan: ${data.statusAdmin}",
                                        color = Color.Red,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                } else {
                                    Text(
                                        text = "Status laporan final dan tidak dapat diubah kembali.",
                                        color = Color.Gray,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
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
                        OutlinedTextField(value = alasanTolak, onValueChange = { alasanTolak = it },
                            placeholder = { Text("Alasan penolakan...") })
                    },
                    confirmButton = {
                        Button(onClick = {
                            coroutineScope.launch {
                                repository.updateStatusLaporan(data.id, "Ditolak", alasanTolak)
                                laporan = data.copy(status = "Ditolak", statusAdmin = alasanTolak)
                                showDialogTolak = false
                            }
                        }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
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
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
        }
    }
}

@Composable
fun StatusBadgeAdmin(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Selesai" -> Color(0xFFDCFCE7) to Color(0xFF166534)
        "Diproses" -> Color(0xFFDBEAFE) to Color(0xFF1E40AF)
        "Diverifikasi" -> Color(0xFFE0F2FE) to Color(0xFF075985)
        "Ditolak" -> Color(0xFFFEE2E2) to Color(0xFF991B1B)
        "Menunggu" -> Color(0xFFFEF3C7) to Color(0xFF92400E)
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
fun UrgensiBadgeAdmin(urgensi: String) {
    val (backgroundColor, textColor) = when (urgensi) {
        "Tinggi" -> Color(0xFFFEE2E2) to Color(0xFFEF4444)
        "Sedang" -> Color(0xFFFEF3C7) to Color(0xFFF59E0B)
        "Rendah" -> Color(0xFFDCFCE7) to Color(0xFF22C55E)
        else -> Color(0xFFF1F3F5) to Color(0xFF6C757D)
    }

    Surface(color = backgroundColor, shape = RoundedCornerShape(16.dp)) {
        Text(
            text = urgensi,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun StatusOptionButtonAdmin(
    label: String,
    isSelected: Boolean,
    modifier: Modifier,
    selectedColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, if (isSelected) selectedColor else Color(0xFFD1D5DB)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) selectedColor else Color(0xFFF1F3F5)
        )
    ) {
        Text(label, fontSize = 12.sp, color = if (isSelected) Color.White else Color(0xFF6C757D))
    }
}

@Composable
fun ChipAdmin(text: String) {
    Surface(color = Color(0xFFF1F3F5), shape = RoundedCornerShape(16.dp)) {
        Text(text = text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6C757D))
    }
}