package com.app.rrq.ui.pages.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import com.app.rrq.ui.pages.AdminBottomBar
import com.app.rrq.ui.theme.BackgroundGray
import com.app.rrq.ui.theme.RoadResQTheme
import com.app.rrq.ui.theme.TealPrimary
import com.app.rrq.ui.theme.TextPrimary
import com.app.rrq.ui.theme.TextSecondary
import com.app.rrq.data.UserData
import com.app.rrq.model.Role
import com.app.rrq.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaPenggunaPage(onNavigate: (Int) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }

    val userList = UserData.dataUser
    var selectedTab by remember { mutableIntStateOf(2) }

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = {
            AdminBottomBar(selected = selectedTab, onSelect = {
                selectedTab = it
                onNavigate(it)
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.Companion.padding(24.dp)) {
                Text(
                    text = "Kelola Pengguna",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Companion.Bold,
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
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF3F4F6),
                    unfocusedContainerColor = Color(0xFFF3F4F6),
                    focusedBorderColor = Color.Companion.Transparent,
                    unfocusedBorderColor = Color.Companion.Transparent,
                )
            )

            Spacer(modifier = Modifier.Companion.height(24.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(userList) { User ->
                    UserCardCustom(User)
                }
            }
        }
    }
}

@Composable
fun UserCardCustom(User: User) {
    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Companion.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.Companion.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                Box(
                    modifier = Modifier.Companion
                        .size(60.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(TealPrimary),
                    contentAlignment = Alignment.Companion.Center
                ) {
                    Text(
                        text = User.name.take(1).uppercase(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color.Companion.White
                    )
                }

                Spacer(modifier = Modifier.Companion.width(16.dp))

                Column {
                    Text(
                        text = User.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.Companion.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = Color(0xFFDCFCE7),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = User.status.value,
                                modifier = Modifier.Companion.padding(
                                    horizontal = 10.dp,
                                    vertical = 2.dp
                                ),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Companion.Bold,
                                color = Color(0xFF16A34A)
                            )
                        }

                        val warnaRole: Color
                        if (User.role.value.equals(Role.ADMIN.value)){
                            warnaRole = Color.Red
                        }
                        else{
                            warnaRole = Color.Green
                        }

                        Surface(
                            color = warnaRole,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = User.role.value,
                                modifier = Modifier.padding(
                                    horizontal = 15.dp,
                                    vertical = 2.dp
                                ),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.Companion.height(16.dp))

            UserInfoRow(icon = Icons.Default.Email, text = User.email)
            Spacer(modifier = Modifier.Companion.height(8.dp))
            UserInfoRow(icon = Icons.Default.Phone, text = User.phone)
            Spacer(modifier = Modifier.Companion.height(8.dp))
            UserInfoRow(icon = Icons.Default.CalendarToday, text = "Daftar ${User.date}")

            Spacer(modifier = Modifier.Companion.height(20.dp))

            Row(modifier = Modifier.Companion.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.Companion.weight(1f).height(48.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.5f))
                ) {
                    Icon(
                        Icons.Default.Block,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.Companion.size(18.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Text("Ban", color = Color(0xFFD97706))
                }
                Spacer(modifier = Modifier.Companion.width(12.dp))
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.Companion.weight(1f).height(48.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.2f))
                ) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.Companion.size(18.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Text("Hapus", color = Color(0xFFEF4444))
                }
            }
        }
    }
}

@Composable
fun UserInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.Companion.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.Companion.size(18.dp)
        )
        Spacer(modifier = Modifier.Companion.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = TextSecondary)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewKelolaPenggunaPage() {
    RoadResQTheme {
        KelolaPenggunaPage()
    }
}