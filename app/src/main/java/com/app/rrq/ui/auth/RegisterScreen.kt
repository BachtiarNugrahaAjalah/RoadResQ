package com.app.rrq.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rrq.data.repository.Resource

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit = {}
) {
    val viewModel: RegisterViewModel = viewModel()
    val context = LocalContext.current
    val registerState by viewModel.registerState.observeAsState()

    var namaLengkap by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nomorTelepon by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var konfirmasiVisible by remember { mutableStateOf(false) }
    var setujuSyarat by remember { mutableStateOf(false) }

    var namaError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var teleponError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var konfirmasiError by remember { mutableStateOf("") }
    var syaratError by remember { mutableStateOf("") }

    LaunchedEffect(registerState) {
        when (val state = registerState) {
            is Resource.Success -> {
                Toast.makeText(context, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            }
            is Resource.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    fun validate(): Boolean {
        var valid = true
        namaError = ""; emailError = ""; teleponError = ""
        passwordError = ""; konfirmasiError = ""; syaratError = ""

        if (namaLengkap.isBlank()) { namaError = "Nama lengkap tidak boleh kosong"; valid = false }
        if (email.isBlank()) {
            emailError = "Email tidak boleh kosong"; valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Format email tidak valid"; valid = false
        }
        if (nomorTelepon.isBlank()) {
            teleponError = "Nomor telepon tidak boleh kosong"; valid = false
        } else if (!nomorTelepon.startsWith("08") || nomorTelepon.length < 10) {
            teleponError = "Nomor telepon tidak valid"; valid = false
        }
        if (password.isBlank()) {
            passwordError = "Password tidak boleh kosong"; valid = false
        } else if (password.length < 6) {
            passwordError = "Password minimal 6 karakter"; valid = false
        }
        if (konfirmasiPassword.isBlank()) {
            konfirmasiError = "Konfirmasi password tidak boleh kosong"; valid = false
        } else if (konfirmasiPassword != password) {
            konfirmasiError = "Password tidak cocok"; valid = false
        }
        if (!setujuSyarat) { syaratError = "Anda harus menyetujui syarat dan ketentuan"; valid = false }

        return valid
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RoadResQLightBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 48.dp, bottom = 32.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Buat Akun", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                "Bergabung dan mulai bantu jalan jadi lebih baik.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(28.dp))

            AuthTextField(
                label = "Nama Lengkap",
                value = namaLengkap,
                onValueChange = { namaLengkap = it; namaError = "" },
                placeholder = "Achira Lucy",
                errorMessage = namaError
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it; emailError = "" },
                placeholder = "achira@gmail.com",
                errorMessage = emailError,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                label = "Nomor Telepon",
                value = nomorTelepon,
                onValueChange = { nomorTelepon = it; teleponError = "" },
                placeholder = "08xxxxxxxxxx",
                errorMessage = teleponError,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Password", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = "" },
                placeholder = { Text("Min. 6 karakter", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = passwordError.isNotEmpty(),
                supportingText = {
                    if (passwordError.isNotEmpty()) Text(passwordError, color = MaterialTheme.colorScheme.error)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null, tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                    focusedBorderColor = RoadResQTeal, unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                    cursorColor = RoadResQTeal
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Konfirmasi Password", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = konfirmasiPassword,
                onValueChange = { konfirmasiPassword = it; konfirmasiError = "" },
                placeholder = { Text("Ulangi password", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (konfirmasiVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = konfirmasiError.isNotEmpty(),
                supportingText = {
                    if (konfirmasiError.isNotEmpty()) Text(konfirmasiError, color = MaterialTheme.colorScheme.error)
                },
                trailingIcon = {
                    IconButton(onClick = { konfirmasiVisible = !konfirmasiVisible }) {
                        Icon(
                            imageVector = if (konfirmasiVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null, tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black, unfocusedTextColor = Color.Black,
                    focusedBorderColor = RoadResQTeal, unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                    cursorColor = RoadResQTeal
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                Checkbox(
                    checked = setujuSyarat,
                    onCheckedChange = { setujuSyarat = it; syaratError = "" },
                    colors = CheckboxDefaults.colors(checkedColor = RoadResQTeal, uncheckedColor = Color.Gray)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(color = Color.Gray, fontSize = 13.sp)) { append("Saya menyetujui ") }
                        withStyle(SpanStyle(color = RoadResQTeal, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)) { append("syarat dan ketentuan") }
                        withStyle(SpanStyle(color = Color.Gray, fontSize = 13.sp)) { append(" serta ") }
                        withStyle(SpanStyle(color = RoadResQTeal, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)) { append("kebijakan privasi") }
                        withStyle(SpanStyle(color = Color.Gray, fontSize = 13.sp)) { append(" RoadResQ.") }
                    })
                    if (syaratError.isNotEmpty()) {
                        Text(syaratError, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { if (validate()) viewModel.register(namaLengkap, email, nomorTelepon, password) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = registerState !is Resource.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = RoadResQTeal)
            ) {
                if (registerState is Resource.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Text("Daftar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(onClick = onNavigateToLogin, modifier = Modifier.fillMaxWidth()) {
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.Gray)) { append("Sudah punya akun? ") }
                    withStyle(SpanStyle(color = RoadResQTeal, fontWeight = FontWeight.SemiBold)) { append("Masuk") }
                }, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    errorMessage: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Text(
        text = label,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        isError = errorMessage.isNotEmpty(),
        supportingText = {
            if (errorMessage.isNotEmpty())
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedBorderColor = RoadResQTeal,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = RoadResQTeal
        )
    )
}