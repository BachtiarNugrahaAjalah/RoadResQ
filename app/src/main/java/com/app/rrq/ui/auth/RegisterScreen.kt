package com.app.rrq.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit = {}
) {
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

    fun validate(): Boolean {
        var valid = true
        namaError = ""; emailError = ""; teleponError = ""
        passwordError = ""; konfirmasiError = ""; syaratError = ""

        if (namaLengkap.isBlank()) {
            namaError = "Nama lengkap tidak boleh kosong"; valid = false
        }
        if (email.isBlank()) {
            emailError = "Email tidak boleh kosong"; valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Format email tidak valid"; valid = false
        }
        if (nomorTelepon.isBlank()) {
            teleponError = "Nomor telepon tidak boleh kosong"; valid = false
        } else if (!nomorTelepon.startsWith("08") || nomorTelepon.length < 10) {
            teleponError = "Nomor telepon tidak valid (contoh: 08xxxxxxxxxx)"; valid = false
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
        if (!setujuSyarat) {
            syaratError = "Anda harus menyetujui syarat dan ketentuan"; valid = false
        }
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
            IconButton(
                onClick = onBack,
                modifier = Modifier.offset(x = (-12).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color(0xFF1A1A2E)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Buat Akun",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Bergabung dan mulai bantu jalan jadi lebih baik.",
                fontSize = 14.sp,
                color = Color(0xFF6B7280)
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

            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A2E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = "" },
                placeholder = { Text("Min. 6 karakter", color = Color(0xFFADB5BD)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = passwordError.isNotEmpty(),
                supportingText = {
                    if (passwordError.isNotEmpty())
                        Text(passwordError, color = MaterialTheme.colorScheme.error)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF6B7280)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoadResQTeal,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Konfirmasi Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A2E)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = konfirmasiPassword,
                onValueChange = { konfirmasiPassword = it; konfirmasiError = "" },
                placeholder = { Text("Ulangi password", color = Color(0xFFADB5BD)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (konfirmasiVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = konfirmasiError.isNotEmpty(),
                supportingText = {
                    if (konfirmasiError.isNotEmpty())
                        Text(konfirmasiError, color = MaterialTheme.colorScheme.error)
                },
                trailingIcon = {
                    IconButton(onClick = { konfirmasiVisible = !konfirmasiVisible }) {
                        Icon(
                            imageVector = if (konfirmasiVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color(0xFF6B7280)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RoadResQTeal,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = setujuSyarat,
                    onCheckedChange = { setujuSyarat = it; syaratError = "" },
                    colors = CheckboxDefaults.colors(
                        checkedColor = RoadResQTeal,
                        uncheckedColor = Color(0xFFADB5BD)
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xFF6B7280), fontSize = 13.sp)) {
                                append("Saya menyetujui ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = RoadResQTeal,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            ) {
                                append("syarat dan ketentuan")
                            }
                            withStyle(SpanStyle(color = Color(0xFF6B7280), fontSize = 13.sp)) {
                                append(" serta ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = RoadResQTeal,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            ) {
                                append("kebijakan privasi")
                            }
                            withStyle(SpanStyle(color = Color(0xFF6B7280), fontSize = 13.sp)) {
                                append(" RoadResQ.")
                            }
                        }
                    )
                    if (syaratError.isNotEmpty()) {
                        Text(
                            text = syaratError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (validate()) {
                        onRegisterSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoadResQTeal
                )
            ) {
                Text(
                    text = "Daftar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(color = Color(0xFF6B7280))) {
                            append("Sudah punya akun? ")
                        }
                        withStyle(
                            SpanStyle(
                                color = RoadResQTeal,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Masuk")
                        }
                    },
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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
        color = Color(0xFF1A1A2E)
    )
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFFADB5BD)) },
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
            focusedBorderColor = RoadResQTeal,
            unfocusedBorderColor = Color(0xFFE0E0E0),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}