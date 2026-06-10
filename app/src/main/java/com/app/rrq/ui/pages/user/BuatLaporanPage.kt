package com.app.rrq.ui.pages.user

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.ui.pages.UserBottomBar
import com.app.rrq.ui.theme.BackgroundGray
import com.app.rrq.ui.theme.RoadResQTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatLaporanPage(
    modifier: Modifier = Modifier,
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
            // Header
            Column {
                Text(
                    text = "Buat Laporan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212529)
                )
                Text(
                    text = "Isi detail kerusakan jalan",
                    fontSize = 14.sp,
                    color = Color(0xFF6C757D)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- FOTO KERUSAKAN ---
            FormLabel("Foto Kerusakan")
            PhotoUploadSection(onClick = { /* Logic ambil foto */ })

            Spacer(modifier = Modifier.height(24.dp))

            // --- JUDUL LAPORAN ---
            FormLabel("Judul Laporan")
            FormTextField(
                value = judul,
                onValueChange = { judul = it },
                placeholder = "Contoh: Lubang besar di Jl. Sudirman"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- KATEGORI KERUSAKAN ---
            FormLabel("Kategori Kerusakan")
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = kategori.ifEmpty { "Pilih kategori" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(isPlaceholder = kategori.isEmpty())
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selectionOption,
                                    color = if (selectionOption == kategori) Color.White else Color.Black
                                )
                            },
                            onClick = {
                                kategori = selectionOption
                                expanded = false
                            },
                            modifier = Modifier.background(
                                if (selectionOption == kategori) Color(0xFF0D6EFD) else Color.Transparent
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TINGKAT URGENSI ---
            FormLabel("Tingkat Urgensi")
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
                        isSelected = urgensi == text,
                        selectedColor = color,
                        modifier = Modifier.weight(1f),
                        onClick = { urgensi = text }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- LOKASI KEJADIAN ---
            FormLabel("Lokasi Kejadian")
            FormTextField(
                value = lokasi,
                onValueChange = { lokasi = it },
                placeholder = "Jl. Contoh No. 123, Kota",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFFADB5BD)
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- DESKRIPSI ---
            FormLabel("Deskripsi")
            FormTextField(
                value = deskripsi,
                onValueChange = { deskripsi = it },
                placeholder = "Jelaskan kondisi jalan secara detail...",
                minHeight = 120.dp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- SUBMIT BUTTON ---
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
                            onNavigate(2)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Kirim Laporan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- REUSABLE COMPONENTS ---

@Composable
fun FormLabel(text: String) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        color = Color(0xFF495057),
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    minHeight: Dp = Dp.Unspecified
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFFADB5BD)) },
        leadingIcon = leadingIcon,
        modifier = if (minHeight != Dp.Unspecified) {
            Modifier.fillMaxWidth().height(minHeight)
        } else {
            Modifier.fillMaxWidth()
        },
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors()
    )
}

@Composable
fun UrgencyButton(
    text: String,
    isSelected: Boolean,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        color = if (isSelected) selectedColor else Color(0xFFF1F3F5),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = if (isSelected) Color.White else Color(0xFF495057),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun PhotoUploadSection(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = Color(0xFFCED4DA),
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                ),
                cornerRadius = CornerRadius(12.dp.toPx())
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFFADB5BD),
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Tap untuk ambil foto",
                color = Color(0xFF495057),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
            Text(
                text = "JPG, PNG · Maks 5MB",
                color = Color(0xFFADB5BD),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun textFieldColors(isPlaceholder: Boolean = false) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF0D6EFD),
    unfocusedBorderColor = Color(0xFFE9ECEF),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedTextColor = if (isPlaceholder) Color(0xFFADB5BD) else Color.Black,
    unfocusedTextColor = if (isPlaceholder) Color(0xFFADB5BD) else Color.Black
)

@Preview(showBackground = true)
@Composable
fun BuatLaporanPreview() {
    RoadResQTheme {
        BuatLaporanPage()
    }
}
