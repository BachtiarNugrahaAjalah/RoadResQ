package com.app.rrq.ui.pages.user

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.ui.pages.UserBottomBar
import com.app.rrq.ui.theme.BackgroundGray
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatLaporanPage(
    modifier: Modifier = Modifier.Companion,
    onNavigate: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var urgensi by remember { mutableStateOf("Sedang") }
    var lokasi by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val categories = listOf("Lubang", "Retak", "Aspal Mengelupas", "Banjir", "Penerangan Rusak", "Marka Pudar", "Lainnya")

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
            Row(
                verticalAlignment = Alignment.Companion.CenterVertically,
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Buat Laporan",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color(0xFF212529)
                    )
                    Text(
                        text = "Isi detail kerusakan jalan",
                        fontSize = 14.sp,
                        color = Color(0xFF6C757D)
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(32.dp))

            // --- FOTO KERUSAKAN ---
            Text(
                text = "Foto Kerusakan",
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF495057),
                modifier = Modifier.Companion.padding(bottom = 10.dp)
            )
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Companion.White)
                    .clickable { }
            ) {
                Canvas(modifier = Modifier.Companion.fillMaxSize()) {
                    val stroke = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.Companion.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                    )
                    drawRoundRect(
                        color = Color(0xFFCED4DA),
                        style = stroke,
                        cornerRadius = CornerRadius(12.dp.toPx())
                    )
                }
                Column(
                    modifier = Modifier.Companion.fillMaxSize(),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(0xFFADB5BD),
                        modifier = Modifier.Companion.size(36.dp)
                    )
                    Spacer(modifier = Modifier.Companion.height(8.dp))
                    Text(
                        text = "Tap untuk ambil foto",
                        color = Color(0xFF495057),
                        fontWeight = FontWeight.Companion.Medium,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "JPG, PNG · Maks 5MB",
                        color = Color(0xFFADB5BD),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // --- JUDUL LAPORAN ---
            Text(
                text = "Judul Laporan",
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF495057),
                modifier = Modifier.Companion.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                placeholder = {
                    Text(
                        "Contoh: Lubang besar di Jl. Sudirman",
                        color = Color(0xFFADB5BD)
                    )
                },
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0D6EFD),
                    unfocusedBorderColor = Color(0xFFE9ECEF),
                    focusedContainerColor = Color.Companion.White,
                    unfocusedContainerColor = Color.Companion.White
                )
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // --- KATEGORI KERUSAKAN ---
            Text(
                text = "Kategori Kerusakan",
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF495057),
                modifier = Modifier.Companion.padding(bottom = 10.dp)
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.Companion.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = kategori.ifEmpty { "Pilih kategori" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.Companion
                        .menuAnchor(MenuAnchorType.Companion.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0D6EFD),
                        unfocusedBorderColor = Color(0xFFE9ECEF),
                        focusedContainerColor = Color.Companion.White,
                        unfocusedContainerColor = Color.Companion.White,
                        focusedTextColor = if (kategori.isEmpty()) Color(0xFFADB5BD) else Color.Companion.Black,
                        unfocusedTextColor = if (kategori.isEmpty()) Color(0xFFADB5BD) else Color.Companion.Black
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.Companion.background(Color.Companion.White)
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selectionOption,
                                    color = if (selectionOption == kategori) Color.Companion.White else Color.Companion.Black
                                )
                            },
                            onClick = {
                                kategori = selectionOption
                                expanded = false
                            },
                            modifier = Modifier.Companion.background(
                                if (selectionOption == kategori) Color(0xFF0D6EFD) else Color.Companion.Transparent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // --- TINGKAT URGENSI ---
            Text(
                text = "Tingkat Urgensi",
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF495057),
                modifier = Modifier.Companion.padding(bottom = 10.dp)
            )
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UrgencyButton(
                    "Rendah",
                    urgensi == "Rendah",
                    Color(0xFF198754),
                    Modifier.Companion.weight(1f)
                ) { urgensi = "Rendah" }
                UrgencyButton(
                    "Sedang",
                    urgensi == "Sedang",
                    Color(0xFFFD7E14),
                    Modifier.Companion.weight(1f)
                ) { urgensi = "Sedang" }
                UrgencyButton(
                    "Tinggi",
                    urgensi == "Tinggi",
                    Color(0xFFDC3545),
                    Modifier.Companion.weight(1f)
                ) { urgensi = "Tinggi" }
            }

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // --- LOKASI KEJADIAN ---
            Text(
                text = "Lokasi Kejadian",
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF495057),
                modifier = Modifier.Companion.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = lokasi,
                onValueChange = { lokasi = it },
                placeholder = { Text("Jl. Contoh No. 123, Kota", color = Color(0xFFADB5BD)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFADB5BD)
                    )
                },
                modifier = Modifier.Companion.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0D6EFD),
                    unfocusedBorderColor = Color(0xFFE9ECEF),
                    focusedContainerColor = Color.Companion.White,
                    unfocusedContainerColor = Color.Companion.White
                )
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            // --- DESKRIPSI ---
            Text(
                text = "Deskripsi",
                fontWeight = FontWeight.Companion.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF495057),
                modifier = Modifier.Companion.padding(bottom = 10.dp)
            )
            OutlinedTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                placeholder = {
                    Text(
                        "Jelaskan kondisi jalan secara detail...",
                        color = Color(0xFFADB5BD)
                    )
                },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(120.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF0D6EFD),
                    unfocusedBorderColor = Color(0xFFE9ECEF),
                    focusedContainerColor = Color.Companion.White,
                    unfocusedContainerColor = Color.Companion.White
                )
            )

            Spacer(modifier = Modifier.Companion.height(32.dp))

            Button(
                onClick = {
                    if (!isLoading) {
                        scope.launch {
                            isLoading = true
                            delay(2000)
                            isLoading = false
                            Toast.makeText(
                                context,
                                "Laporan berhasil ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()
                            onNavigateBack()
                        }
                    }
                },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.Companion.size(24.dp),
                        color = Color.Companion.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Kirim Laporan", fontSize = 16.sp, fontWeight = FontWeight.Companion.Bold)
                }
            }
        }
    }
}

@Composable
fun UrgencyButton(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    modifier: Modifier = Modifier.Companion,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (isSelected) selectedColor else Color(0xFFF1F3F5),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Box(contentAlignment = Alignment.Companion.Center) {
            Text(
                text = text,
                color = if (isSelected) Color.Companion.White else Color(0xFF495057),
                fontWeight = if (isSelected) FontWeight.Companion.Bold else FontWeight.Companion.Medium,
                fontSize = 14.sp
            )
        }
    }
}