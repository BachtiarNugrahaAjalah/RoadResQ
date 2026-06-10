package com.app.rrq.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rrq.R
import com.app.rrq.data.repository.Resource

val RoadResQTeal = Color(0xFF0C8FBA)
val RoadResQLightBg = Color(0xFFF0F4F8)

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val loginState by viewModel.loginState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }

    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is Resource.Success -> state.data?.let { onLoginSuccess(it) }
            is Resource.Error -> Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
            else -> {}
        }
    }

    fun validate(): Boolean {
        var valid = true
        emailError = ""
        passwordError = ""
        if (email.isBlank()) {
            emailError = "Email tidak boleh kosong"
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Format email tidak valid"
            valid = false
        }
        if (password.isBlank()) {
            passwordError = "Password tidak boleh kosong"
            valid = false
        } else if (password.length < 6) {
            passwordError = "Password minimal 6 karakter"
            valid = false
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
            IconButton(onClick = {}, modifier = Modifier.offset(x = (-12).dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Selamat Datang",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Masuk ke akun RoadResQ Anda",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(text = "Email", fontWeight = FontWeight.SemiBold, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = "" },
                placeholder = { Text("nama@gmail.com", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                singleLine = true,
                isError = emailError.isNotEmpty(),
                supportingText = {
                    if (emailError.isNotEmpty())
                        Text(emailError, color = MaterialTheme.colorScheme.error)
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = RoadResQTeal,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = RoadResQTeal
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Password", fontWeight = FontWeight.SemiBold, color = Color.Black)

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = "" },
                placeholder = { Text("••••••••", color = Color.Gray) },
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
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedBorderColor = RoadResQTeal,
                    unfocusedBorderColor = Color.LightGray,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = RoadResQTeal
                )
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { if (validate()) viewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                enabled = loginState !is Resource.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = RoadResQTeal)
            ) {
                if (loginState is Resource.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Masuk", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Belum punya akun? ", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "Daftar",
                    fontSize = 14.sp,
                    color = RoadResQTeal,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}