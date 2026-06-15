package com.app.rrq.ui.pages.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rrq.ui.pages.UserBottomBar
import com.app.rrq.ui.theme.BackgroundGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatLaporanPage(
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val repository = remember { LaporanRepository() }
    val categories = listOf("Lubang", "Retak", "Aspal Mengelupas", "Banjir", "Penerangan Rusak", "Marka Pudar", "Lainnya")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            scope.launch {
                val base64 = uriToBase64(context, it)
                if (base64 != null) {
                    // Simpan data mentah Base64 (tanpa prefix)
                    base64Image = base64
                }
            }
        }
    }

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = {
            UserBottomBar(selected = 1, onSelect = onNavigate)
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Column {
                Text(text = "Buat Laporan", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF212529))
                Text(text = "Isi detail kerusakan jalan", fontSize = 14.sp, color = Color(0xFF6C757D))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "Foto Kerusakan", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF495057))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable {
                        Toast.makeText(context, "Fitur kamera akan segera hadir", Toast.LENGTH_SHORT).show()
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))
                    drawRoundRect(color = Color(0xFFCED4DA), style = stroke, cornerRadius = CornerRadius(12.dp.toPx()))
                }
                Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color(0xFFADB5BD), modifier = Modifier.size(36.dp))
                    Text(text = "Tap untuk ambil foto", color = Color(0xFF495057), fontWeight = FontWeight.Medium, fontSize = 15.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Judul Laporan", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF495057))
            OutlinedTextField(
                value = viewModel.judul,
                onValueChange = { viewModel.onJudulChange(it) },
                placeholder = { Text("Contoh: Lubang besar di Jl. Sudirman", color = Color(0xFFADB5BD)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Kategori Kerusakan", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF495057))
            ExposedDropdownMenuBox(
                expanded = viewModel.expanded,
                onExpandedChange = { viewModel.onExpandedChange(it) },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = viewModel.kategori.ifEmpty { "Pilih kategori" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = viewModel.expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = viewModel.expanded,
                    onDismissRequest = { viewModel.onExpandedChange(false) },
                    modifier = Modifier.background(Color.White)
                ) {
                    categories.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(text = option) },
                            onClick = {
                                viewModel.onKategoriChange(option)
                                viewModel.onExpandedChange(false)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Tingkat Urgensi", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF495057))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val urgencyOptions = listOf(
                    "Rendah" to Color(0xFF198754),
                    "Sedang" to Color(0xFFFD7E14),
                    "Tinggi" to Color(0xFFDC3545)
                )
                urgencyOptions.forEach { (text, color) ->
                    UrgencyButton(
                        text = text,
                        isSelected = viewModel.urgensi == text,
                        selectedColor = color,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onUrgensiChange(text) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Lokasi Kejadian", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF495057))
            OutlinedTextField(
                value = viewModel.lokasi,
                onValueChange = { viewModel.onLokasiChange(it) },
                leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = Color(0xFFADB5BD)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Deskripsi", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF495057))
            OutlinedTextField(
                value = viewModel.deskripsi,
                onValueChange = { viewModel.onDeskripsiChange(it) },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (viewModel.judul.isBlank() || viewModel.kategori.isBlank() || viewModel.lokasi.isBlank()) {
                        Toast.makeText(context, "Harap isi semua data wajib!", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.kirimLaporan {
                            Toast.makeText(context, "Laporan berhasil dikirim!", Toast.LENGTH_SHORT).show()
                            onNavigateBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                enabled = !viewModel.isLoading
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Kirim Laporan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun UrgencyButton(text: String, isSelected: Boolean, selectedColor: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier.height(48.dp).clip(RoundedCornerShape(12.dp)).clickable { onClick() },
        color = if (isSelected) selectedColor else Color(0xFFF1F3F5),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, color = if (isSelected) Color.White else Color(0xFF495057), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium, fontSize = 14.sp)
        }
    }
}
