package com.app.rrq.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.rrq.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaPenggunaPage(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    val userList = listOf(
        UserAccountData("Indriyani", "indritp3@gmail.com", "089504633633", "24 Apr 2026"),
        UserAccountData("Rapli", "tbuyut0@gmail.com", "08157234946", "23 Apr 2026")
    )

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = {
            AdminBottomBar(selected = 2, onSelect = { })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "Kelola Pengguna",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${userList.size} pengguna terdaftar",
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari nama atau email...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(userList) { user ->
                    UserCardCustom(user)
                }
            }
        }
    }
}

@Composable
fun UserCardCustom(user: UserAccountData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(TealPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.take(1).uppercase(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = "Aktif",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF16A34A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(16.dp))

            UserInfoRow(icon = Icons.Default.Email, text = user.email)
            Spacer(modifier = Modifier.height(8.dp))
            UserInfoRow(icon = Icons.Default.Phone, text = user.phone)
            Spacer(modifier = Modifier.height(8.dp))
            UserInfoRow(icon = Icons.Default.CalendarToday, text = "Daftar ${user.date}")

            Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.5f))
                ) {
                    Icon(Icons.Default.Block, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ban", color = Color(0xFFD97706))
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hapus", color = Color(0xFFEF4444))
                }
            }
        }
    }
}

@Composable
fun UserInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = TextSecondary)
    }
}

data class UserAccountData(
    val name: String,
    val email: String,
    val phone: String,
    val date: String
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewKelolaPenggunaPage() {
    RoadResQTheme {
        KelolaPenggunaPage(navController = rememberNavController())
    }
}
