package com.app.rrq.ui.pages

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.app.rrq.R
import com.app.rrq.data.local.SessionManager
import com.app.rrq.model.ProfilState
import com.app.rrq.model.ProfilViewModel
import com.app.rrq.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfilPage(
    onBack: () -> Unit,
    viewModel: ProfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    var nama by remember { mutableStateOf(session.getNama()) }
    var email by remember { mutableStateOf(session.getEmail()) }
    var namaError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }

    // Dialog konfirmasi password untuk ganti email
    var showPasswordDialog by remember { mutableStateOf(false) }
    var pendingEmail by remember { mutableStateOf("") }

    // Foto profil
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val photoUrl = remember { session.getPhotoUrl() }
    var showPhotoOptions by remember { mutableStateOf(false) }
    var photoUploaded by remember { mutableStateOf(false) }

    // Launcher untuk pilih gambar dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            photoUri = it
            photoUploaded = false
            viewModel.uploadFoto(context, it) {
                photoUploaded = true
            }
        }
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) galleryLauncher.launch("image/*")
        else Toast.makeText(context, "Izin akses galeri ditolak", Toast.LENGTH_SHORT).show()
    }

    fun requestGallery() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else
            Manifest.permission.READ_EXTERNAL_STORAGE
        permissionLauncher.launch(permission)
    }

    // Tangani state perubahan
    LaunchedEffect(state) {
        when (val s = state) {
            is ProfilState.Success -> {
                Toast.makeText(context, "Berhasil disimpan!", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            is ProfilState.Error -> {
                Toast.makeText(context, s.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    // Dialog konfirmasi password untuk ganti email
    if (showPasswordDialog) {
        EmailPasswordConfirmDialog(
            onDismiss = { showPasswordDialog = false },
            onConfirm = { password ->
                showPasswordDialog = false
                viewModel.updateEmail(context, pendingEmail, password)
            }
        )
    }

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Profil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TealPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header foto profil ──────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(TealPrimary, TealDark)),
                    )
                    .padding(vertical = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                                .clickable { showPhotoOptions = true },
                            contentAlignment = Alignment.Center
                        ) {
                            val displayUri = photoUri
                            val displayUrl = photoUrl
                            when {
                                displayUri != null -> Image(
                                    painter = rememberAsyncImagePainter(displayUri),
                                    contentDescription = "Foto Profil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                displayUrl.isNotEmpty() -> Image(
                                    painter = rememberAsyncImagePainter(displayUrl),
                                    contentDescription = "Foto Profil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                else -> Text(
                                    text = if (nama.isNotEmpty()) nama[0].toString().uppercase() else "?",
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        // Tombol kamera
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(OrangeAccent)
                                .clickable { showPhotoOptions = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Ganti foto",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ketuk foto untuk mengubah",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
            }

            // Loading indicator upload foto
            if (state is ProfilState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = TealPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Form edit ───────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Nama
                    Text(
                        text = "Nama Pengguna",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it; namaError = "" },
                        placeholder = { Text("Masukkan nama lengkap") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = TealPrimary)
                        },
                        isError = namaError.isNotEmpty(),
                        supportingText = {
                            if (namaError.isNotEmpty())
                                Text(namaError, color = MaterialTheme.colorScheme.error)
                        },
                        colors = profileTextFieldColors()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email
                    Text(
                        text = "Email",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = "" },
                        placeholder = { Text("Masukkan email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = TealPrimary)
                        },
                        isError = emailError.isNotEmpty(),
                        supportingText = {
                            if (emailError.isNotEmpty())
                                Text(emailError, color = MaterialTheme.colorScheme.error)
                        },
                        colors = profileTextFieldColors()
                    )

                    // Peringatan ganti email
                    AnimatedVisibility(visible = email != session.getEmail()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFFF3CD))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF856404),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mengganti email memerlukan konfirmasi password Anda.",
                                fontSize = 12.sp,
                                color = Color(0xFF856404)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Tombol Simpan ───────────────────────────────────────
            Button(
                onClick = {
                    // Validasi
                    var valid = true
                    if (nama.isBlank()) { namaError = "Nama tidak boleh kosong"; valid = false }
                    if (email.isBlank()) { emailError = "Email tidak boleh kosong"; valid = false }
                    else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailError = "Format email tidak valid"; valid = false
                    }
                    if (!valid) return@Button

                    // Update nama jika berubah
                    if (nama != session.getNama()) {
                        viewModel.updateNama(context, nama)
                    }
                    // Update email jika berubah – minta konfirmasi password
                    if (email != session.getEmail()) {
                        pendingEmail = email
                        showPasswordDialog = true
                    } else if (nama == session.getNama() && !photoUploaded) {
                        Toast.makeText(context, "Tidak ada perubahan", Toast.LENGTH_SHORT).show()
                    } else if (nama == session.getNama() && photoUploaded) {
                        // Foto sudah diupload, nama dan email tidak berubah
                        Toast.makeText(context, "Foto profil berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        photoUploaded = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = state !is ProfilState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
            ) {
                if (state is ProfilState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Simpan Perubahan", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Bottom sheet pilih sumber foto
    if (showPhotoOptions) {
        PhotoSourceBottomSheet(
            onDismiss = { showPhotoOptions = false },
            onGaleri = {
                showPhotoOptions = false
                requestGallery()
            }
        )
    }
}

/** Dialog konfirmasi password untuk update email */
@Composable
fun EmailPasswordConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = CardWhite,
        title = {
            Text(
                "Konfirmasi Password",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )
        },
        text = {
            Column {
                Text(
                    "Masukkan kata sandi Anda untuk mengkonfirmasi perubahan email.",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; error = "" },
                    placeholder = { Text("Kata sandi") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility
                                else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = TextSecondary
                            )
                        }
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = TealPrimary)
                    },
                    isError = error.isNotEmpty(),
                    supportingText = {
                        if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error)
                    },
                    colors = profileTextFieldColors()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (password.isBlank()) {
                        error = "Password tidak boleh kosong"
                    } else {
                        onConfirm(password)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Konfirmasi", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal", color = TextSecondary)
            }
        }
    )
}

/** Bottom sheet untuk memilih sumber foto profil */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoSourceBottomSheet(
    onDismiss: () -> Unit,
    onGaleri: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardWhite,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                "Ganti Foto Profil",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // Pilih dari galeri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(BackgroundGray)
                    .clickable { onGaleri() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(TealLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = TealPrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Pilih dari Galeri", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                    Text("Pilih foto dari galeri perangkat", fontSize = 12.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol batal
            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary)
            ) {
                Text("Batal")
            }
        }
    }
}

/** Warna konsisten untuk OutlinedTextField di halaman profil */
@Composable
fun profileTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedBorderColor = TealPrimary,
    unfocusedBorderColor = Color(0xFFE5E7EB),
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = TealPrimary
)
