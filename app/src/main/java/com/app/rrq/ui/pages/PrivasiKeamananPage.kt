package com.app.rrq.ui.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rrq.model.ProfilState
import com.app.rrq.model.ProfilViewModel
import com.app.rrq.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivasiKeamananPage(
    onBack: () -> Unit,
    viewModel: ProfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    var passwordLama by remember { mutableStateOf("") }
    var passwordBaru by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }

    var passwordLamaVisible by remember { mutableStateOf(false) }
    var passwordBaruVisible by remember { mutableStateOf(false) }
    var konfirmasiVisible by remember { mutableStateOf(false) }

    var passwordLamaError by remember { mutableStateOf("") }
    var passwordBaruError by remember { mutableStateOf("") }
    var konfirmasiError by remember { mutableStateOf("") }

    // Strength indicator
    val passwordStrength = remember(passwordBaru) {
        when {
            passwordBaru.length >= 12 && passwordBaru.any { it.isDigit() } &&
            passwordBaru.any { it.isUpperCase() } && passwordBaru.any { !it.isLetterOrDigit() } -> 3
            passwordBaru.length >= 8 -> 2
            passwordBaru.length >= 6 -> 1
            else -> 0
        }
    }

    LaunchedEffect(state) {
        when (val s = state) {
            is ProfilState.Success -> {
                Toast.makeText(context, "Kata sandi berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                passwordLama = ""
                passwordBaru = ""
                konfirmasiPassword = ""
                viewModel.resetState()
            }
            is ProfilState.Error -> {
                Toast.makeText(context, s.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    fun validate(): Boolean {
        var valid = true
        passwordLamaError = ""
        passwordBaruError = ""
        konfirmasiError = ""

        if (passwordLama.isBlank()) {
            passwordLamaError = "Kata sandi lama tidak boleh kosong"
            valid = false
        }
        if (passwordBaru.isBlank()) {
            passwordBaruError = "Kata sandi baru tidak boleh kosong"
            valid = false
        } else if (passwordBaru.length < 6) {
            passwordBaruError = "Kata sandi minimal 6 karakter"
            valid = false
        }
        if (konfirmasiPassword != passwordBaru) {
            konfirmasiError = "Konfirmasi kata sandi tidak cocok"
            valid = false
        }
        if (passwordBaru == passwordLama && passwordBaru.isNotBlank()) {
            passwordBaruError = "Kata sandi baru harus berbeda dari yang lama"
            valid = false
        }
        return valid
    }

    Scaffold(
        containerColor = BackgroundGray,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Privasi & Keamanan",
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
            // ── Header ────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(TealPrimary, TealDark))
                    )
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Keamanan Akun",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        "Jaga akun Anda tetap aman",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }
            }

            if (state is ProfilState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = TealPrimary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Info Card ─────────────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = TealLight),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Untuk keamanan, Anda perlu memasukkan kata sandi lama sebelum menggantinya. " +
                               "Pastikan kata sandi baru minimal 6 karakter.",
                        fontSize = 13.sp,
                        color = TealDark,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Form Ganti Password ────────────────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        "Ubah Kata Sandi",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Password Lama
                    SecurityTextField(
                        label = "Kata Sandi Lama",
                        value = passwordLama,
                        onValueChange = { passwordLama = it; passwordLamaError = "" },
                        visible = passwordLamaVisible,
                        onToggleVisibility = { passwordLamaVisible = !passwordLamaVisible },
                        error = passwordLamaError,
                        placeholder = "Masukkan kata sandi lama"
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFE5E7EB))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Baru
                    SecurityTextField(
                        label = "Kata Sandi Baru",
                        value = passwordBaru,
                        onValueChange = { passwordBaru = it; passwordBaruError = "" },
                        visible = passwordBaruVisible,
                        onToggleVisibility = { passwordBaruVisible = !passwordBaruVisible },
                        error = passwordBaruError,
                        placeholder = "Minimal 6 karakter"
                    )

                    // Password strength indicator
                    if (passwordBaru.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        PasswordStrengthIndicator(strength = passwordStrength)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Konfirmasi Password Baru
                    SecurityTextField(
                        label = "Konfirmasi Kata Sandi Baru",
                        value = konfirmasiPassword,
                        onValueChange = { konfirmasiPassword = it; konfirmasiError = "" },
                        visible = konfirmasiVisible,
                        onToggleVisibility = { konfirmasiVisible = !konfirmasiVisible },
                        error = konfirmasiError,
                        placeholder = "Ulangi kata sandi baru"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Tombol Simpan ──────────────────────────────────────
            Button(
                onClick = {
                    if (validate()) {
                        viewModel.updatePassword(context, passwordLama, passwordBaru)
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
                    Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Perbarui Kata Sandi", color = Color.White, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SecurityTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    error: String,
    placeholder: String
) {
    Text(
        text = label,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary,
        letterSpacing = 0.5.sp
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextSecondary) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        leadingIcon = {
            Icon(Icons.Default.Lock, contentDescription = null, tint = TealPrimary)
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = null,
                    tint = TextSecondary
                )
            }
        },
        isError = error.isNotEmpty(),
        supportingText = {
            if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error)
        },
        colors = profileTextFieldColors()
    )
}

@Composable
private fun PasswordStrengthIndicator(strength: Int) {
    val (label, color) = when (strength) {
        1 -> "Lemah" to Color(0xFFEF4444)
        2 -> "Sedang" to Color(0xFFF59E0B)
        3 -> "Kuat" to Color(0xFF22C55E)
        else -> "Terlalu pendek" to Color(0xFFEF4444)
    }
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(if (index < strength) color else Color(0xFFE5E7EB))
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Kekuatan: $label",
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}
