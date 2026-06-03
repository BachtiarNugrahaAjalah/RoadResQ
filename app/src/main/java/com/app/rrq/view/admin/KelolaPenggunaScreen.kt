package com.app.rrq.view.admin

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
import androidx.compose.material.icons.filled.Check
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
import com.app.rrq.view.AdminBottomBar
import com.app.rrq.core.theme.BackgroundGray
import com.app.rrq.core.theme.RoadResQTheme
import com.app.rrq.core.theme.TealPrimary
import com.app.rrq.core.theme.TextPrimary
import com.app.rrq.core.theme.TextSecondary
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.rrq.core.injection.AppContainer
import com.app.rrq.model.data.Pengguna
import com.app.rrq.viewmodel.admin.KelolaPenggunaState
import com.app.rrq.viewmodel.admin.KelolaPenggunaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaPenggunaScreen(
    onNavigate: (Int) -> Unit = {},
    viewModel: KelolaPenggunaViewModel = viewModel(
        factory = KelolaPenggunaViewModel.provideFactory(
            AppContainer.getPenggunasUseCase,
            AppContainer.updateStatusPenggunaUseCase
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    val userList = (uiState as? KelolaPenggunaState.Success)?.penggunas ?: emptyList()
    val filteredUsers = userList.filter { 
        it.namaLengkap.contains(searchQuery, ignoreCase = true) || 
        it.email.contains(searchQuery, ignoreCase = true) 
    }
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
                    text = "${filteredUsers.size} pengguna terdaftar",
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
                items(filteredUsers) { pengguna ->
                    UserCardCustom(pengguna) { actionId, actionStatus ->
                        viewModel.updateStatus(actionId, actionStatus)
                    }
                }
            }
        }
    }
}

@Composable
fun UserCardCustom(pengguna: Pengguna, onUpdateStatus: (String, String) -> Unit) {
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
                        text = pengguna.namaLengkap.take(1).uppercase(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = Color.Companion.White
                    )
                }

                Spacer(modifier = Modifier.Companion.width(16.dp))

                Column {
                    Text(
                        text = pengguna.namaLengkap,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.Companion.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val (bgColorStatus, textColorStatus) = if (pengguna.status.equals("Aktif", ignoreCase = true) || pengguna.status.equals("ACTIVE", ignoreCase = true)) {
                            Color(0xFFDCFCE7) to Color(0xFF16A34A)
                        } else {
                            Color(0xFFFEE2E2) to Color(0xFFDC2626)
                        }

                        Surface(
                            color = bgColorStatus,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = pengguna.status,
                                modifier = Modifier.Companion.padding(
                                    horizontal = 10.dp,
                                    vertical = 2.dp
                                ),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Companion.Bold,
                                color = textColorStatus
                            )
                        }

                        val (bgColorRole, textColorRole) = if (pengguna.role.equals("Admin", ignoreCase = true)) {
                            Color(0xFFF3E8FF) to Color(0xFF7E22CE)
                        } else {
                            Color(0xFFE0F2FE) to Color(0xFF0369A1)
                        }

                        Surface(
                            color = bgColorRole,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = pengguna.role,
                                modifier = Modifier.padding(
                                    horizontal = 15.dp,
                                    vertical = 2.dp
                                ),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColorRole
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.Companion.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.Companion.height(16.dp))

            UserInfoRow(icon = Icons.Default.Email, text = pengguna.email)
            Spacer(modifier = Modifier.Companion.height(8.dp))
            UserInfoRow(icon = Icons.Default.Phone, text = pengguna.nomorTelepon)
            Spacer(modifier = Modifier.Companion.height(8.dp))
            UserInfoRow(icon = Icons.Default.CalendarToday, text = "Daftar Baru Saja")

            Spacer(modifier = Modifier.Companion.height(20.dp))

            Row(modifier = Modifier.Companion.fillMaxWidth()) {
                OutlinedButton(
                    onClick = { onUpdateStatus(pengguna.id, "Diblokir") },
                    modifier = Modifier.Companion.weight(1f).height(48.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f)),
                    enabled = pengguna.status != "Diblokir"
                ) {
                    Icon(
                        Icons.Default.Block,
                        contentDescription = null,
                        tint = if (pengguna.status != "Diblokir") Color(0xFFEF4444) else Color.Gray,
                        modifier = Modifier.Companion.size(18.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Text("Ban", color = if (pengguna.status != "Diblokir") Color(0xFFEF4444) else Color.Gray)
                }
                Spacer(modifier = Modifier.Companion.width(12.dp))
                OutlinedButton(
                    onClick = { onUpdateStatus(pengguna.id, "Aktif") },
                    modifier = Modifier.Companion.weight(1f).height(48.dp),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFF16A34A).copy(alpha = 0.4f)),
                    enabled = pengguna.status != "Aktif" && pengguna.status != "ACTIVE"
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = if (pengguna.status != "Aktif" && pengguna.status != "ACTIVE") Color(0xFF16A34A) else Color.Gray,
                        modifier = Modifier.Companion.size(18.dp)
                    )
                    Spacer(modifier = Modifier.Companion.width(8.dp))
                    Text("Aktifkan", color = if (pengguna.status != "Aktif" && pengguna.status != "ACTIVE") Color(0xFF16A34A) else Color.Gray)
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
fun PreviewKelolaPenggunaScreen() {
    RoadResQTheme {
        KelolaPenggunaScreen()
    }
}