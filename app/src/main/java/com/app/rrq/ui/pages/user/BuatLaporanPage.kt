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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.scale
import coil.compose.AsyncImage
import com.app.rrq.data.model.Laporan
import com.app.rrq.data.repository.LaporanRepository
import com.app.rrq.ui.pages.UserBottomBar
import com.app.rrq.ui.theme.BackgroundGray
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatLaporanPage(
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {}
) {
    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var urgensi by remember { mutableStateOf("Sedang") }
    var lokasi by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var base64Image by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
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

            FormLabel("Foto Kerusakan")
            PhotoUploadSection(selectedImageUri = selectedImageUri, onClick = { launcher.launch("image/*") })

            Spacer(modifier = Modifier.height(24.dp))

            FormLabel("Judul Laporan")
            FormTextField(value = judul, onValueChange = { judul = it }, placeholder = "Contoh: Lubang besar di Jl. Sudirman")

            Spacer(modifier = Modifier.height(24.dp))

            FormLabel("Kategori Kerusakan")
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = kategori.ifEmpty { "Pilih kategori" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors(isPlaceholder = kategori.isEmpty())
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                    categories.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(text = selectionOption, color = if (selectionOption == kategori) Color.White else Color.Black) },
                            onClick = { kategori = selectionOption; expanded = false },
                            modifier = Modifier.background(if (selectionOption == kategori) Color(0xFF0D6EFD) else Color.Transparent)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormLabel("Tingkat Urgensi")
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                val urgencyOptions = listOf("Rendah" to Color(0xFF198754), "Sedang" to Color(0xFFFD7E14), "Tinggi" to Color(0xFFDC3545))
                urgencyOptions.forEach { (text, color) ->
                    UrgencyButton(text = text, isSelected = urgensi == text, selectedColor = color, modifier = Modifier.weight(1f), onClick = { urgensi = text })
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormLabel("Lokasi Kejadian")
            FormTextField(value = lokasi, onValueChange = { lokasi = it }, placeholder = "Jl. Contoh No. 123, Kota", leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFFADB5BD)) })

            Spacer(modifier = Modifier.height(24.dp))

            FormLabel("Deskripsi")
            FormTextField(value = deskripsi, onValueChange = { deskripsi = it }, placeholder = "Jelaskan kondisi jalan secara detail...", minHeight = 120.dp)

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (judul.isBlank() || kategori.isBlank() || lokasi.isBlank()) {
                        Toast.makeText(context, "Harap isi semua data wajib", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (base64Image.isEmpty()) {
                        Toast.makeText(context, "Harap pilih foto kerusakan", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (!isLoading) {
                        scope.launch {
                            isLoading = true
                            val laporanBaru = Laporan(
                                judulLaporan = judul,
                                kategoriKerusakan = kategori,
                                tingkatUrgensi = urgensi,
                                lokasi = lokasi,
                                deskripsi = deskripsi,
                                gambarUrl = base64Image,
                                tanggal = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                                status = "Menunggu",
                                statusAdmin = ""
                            )
                            val result = repository.simpanLaporan(laporanBaru)
                            isLoading = false
                            if (result.isSuccess) {
                                Toast.makeText(context, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show()
                                onNavigate(2)
                            } else {
                                Toast.makeText(context, "Gagal: ${result.exceptionOrNull()?.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Kirim Laporan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

fun uriToBase64(context: Context, uri: Uri): String? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val originalBitmap = BitmapFactory.decodeStream(inputStream) ?: return null

            // Resize (250px) untuk memastikan kestabilan transfer data Firestore
            val maxSize = 250
            val width = originalBitmap.width
            val height = originalBitmap.height
            val bitmap = if (width > maxSize || height > maxSize) {
                val ratio = width.toFloat() / height.toFloat()
                val newWidth = if (width > height) maxSize else (maxSize * ratio).toInt()
                val newHeight = if (height > width) maxSize else (maxSize / ratio).toInt()
                originalBitmap.scale(newWidth, newHeight, true)
            } else {
                originalBitmap
            }

            val outputStream = ByteArrayOutputStream()
            // Kompresi 40%
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
            val byteArray = outputStream.toByteArray()

            // NO_WRAP agar string tidak mengandung \n
            Base64.encodeToString(byteArray, Base64.NO_WRAP)
        }
    } catch (_: Exception) {
        null
    }
}

@Composable
fun FormLabel(text: String) {
    Text(text = text, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color(0xFF495057), modifier = Modifier.padding(bottom = 10.dp))
}

@Composable
fun FormTextField(value: String, onValueChange: (String) -> Unit, placeholder: String, leadingIcon: @Composable (() -> Unit)? = null, minHeight: Dp = Dp.Unspecified) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFFADB5BD)) },
        leadingIcon = leadingIcon,
        modifier = if (minHeight != Dp.Unspecified) Modifier.fillMaxWidth().height(minHeight) else Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = textFieldColors()
    )
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

@Composable
fun PhotoUploadSection(selectedImageUri: Uri?, onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(12.dp)).background(Color.White).clickable { onClick() }) {
        if (selectedImageUri != null) {
            AsyncImage(model = selectedImageUri, contentDescription = "Selected Image", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(color = Color(0xFFCED4DA), style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)), cornerRadius = CornerRadius(12.dp.toPx()))
            }
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color(0xFFADB5BD), modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Tap untuk ambil foto", color = Color(0xFF495057), fontWeight = FontWeight.Medium, fontSize = 15.sp)
                Text(text = "JPG, PNG · Maks 5MB", color = Color(0xFFADB5BD), fontSize = 12.sp)
            }
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
