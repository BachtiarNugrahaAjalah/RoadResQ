package com.app.rrq.view.auth

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rrq.core.injection.AppContainer
import com.app.rrq.viewmodel.auth.RegisterState
import com.app.rrq.viewmodel.auth.RegisterViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    onNavigateToAdminDashboard: () -> Unit,
    onBack: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.provideFactory(AppContainer.registerUseCase)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    var namaLengkap by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var nomorTelepon by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var konfirmasiPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var konfirmasiVisible by remember { mutableStateOf(false) }
    var setujuSyarat by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is RegisterState.Success) {
            viewModel.resetState()
            onRegisterSuccess()
        }
    }

    val isError = uiState is RegisterState.Error
    val errorMessage = if (isError) (uiState as RegisterState.Error).message else ""
    val isLoading = uiState is RegisterState.Loading

    // We remove the old local validate() function as we use the simple one in ViewModel.
    // Error states are managed by ViewModel.
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
                onValueChange = { namaLengkap = it },
                placeholder = "Achira Lucy",
                errorMessage = if (isError && namaLengkap.isBlank()) errorMessage else ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                placeholder = "achira@gmail.com",
                errorMessage = if (isError && email.isBlank()) errorMessage else "",
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                label = "Nomor Telepon",
                value = nomorTelepon,
                onValueChange = { nomorTelepon = it },
                placeholder = "08xxxxxxxxxx",
                errorMessage = if (isError && nomorTelepon.isBlank()) errorMessage else "",
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
                onValueChange = { password = it },
                placeholder = { Text("Min. 6 karakter", color = Color(0xFFADB5BD)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = isError,
                supportingText = {
                    if (isError)
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
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
                onValueChange = { konfirmasiPassword = it },
                placeholder = { Text("Ulangi password", color = Color(0xFFADB5BD)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (konfirmasiVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = isError,
                supportingText = {
                    if (isError)
                        Text(errorMessage, color = MaterialTheme.colorScheme.error)
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
                    onCheckedChange = { setujuSyarat = it },
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
                    if (isError && !setujuSyarat) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.register(namaLengkap, email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RoadResQTeal
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Daftar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
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