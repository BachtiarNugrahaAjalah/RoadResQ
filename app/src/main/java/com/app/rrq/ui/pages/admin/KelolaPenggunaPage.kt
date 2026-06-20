package com.app.rrq.ui.pages.admin

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.ui.pages.AdminBottomBar
import com.app.rrq.ui.theme.*
import com.app.rrq.data.model.User
import com.app.rrq.data.model.Status
import com.app.rrq.data.repository.UserRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KelolaPenggunaPage(onNavigate: (Int) -> Unit = {}) {
    var searchQuery by remember { mutableStateOf("") }
    var userList by remember { mutableStateOf<List<User>>(emptyList()) }
    val repository = remember { UserRepository() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    
    var loadingUserId by remember { mutableStateOf<String?>(null) }
    var loadingAction by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        val registration = repository.getSemuaUser { list ->
            userList = list
        }
        onDispose { registration.remove() }
    }

    val filteredList = userList.filter {
        it.name.contains(searchQuery, ignoreCase = true) || 
        it.email.contains(searchQuery, ignoreCase = true)
    }

    var selectedTab by remember { mutableIntStateOf(2) }

    Scaffold(
        containerColor = BackgroundGray,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            AdminBottomBar(selected = selectedTab, onSelect = {
                selectedTab = it
                onNavigate(it)
            })
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
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
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
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(filteredList) { user ->
                    UserCardCustom(
                        user = user,
                        isBanLoading = loadingUserId == user.userId && loadingAction == "ban",
                        isDeleteLoading = loadingUserId == user.userId && loadingAction == "delete",
                        onBan = {
                            if (!isInternetAvailable(context)) {
                                scope.launch { snackbarHostState.showSnackbar("Tidak ada koneksi internet") }
                                return@UserCardCustom
                            }
                            scope.launch {
                                loadingUserId = user.userId
                                loadingAction = "ban"
                                val result = repository.banUser(user.email, user.status)

                                loadingUserId = null
                                loadingAction = null

                                val msg = if (result.isSuccess) "Status ${user.name} diperbarui" else "Gagal update status"
                                snackbarHostState.showSnackbar(msg)
                            }
                        },
                        onDelete = {
                            if (!isInternetAvailable(context)) {
                                scope.launch { snackbarHostState.showSnackbar("Tidak ada koneksi internet") }
                                return@UserCardCustom
                            }
                            scope.launch {
                                loadingUserId = user.userId
                                loadingAction = "delete"
                                val result = repository.deleteUser(user.email)
                                
                                loadingUserId = null
                                loadingAction = null
                                
                                val msg = if (result.isSuccess) "User ${user.name} dihapus" else "Gagal menghapus user"
                                snackbarHostState.showSnackbar(msg)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserCardCustom(
    user: User,
    isBanLoading: Boolean,
    isDeleteLoading: Boolean,
    onBan: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6))
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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val statusColor = if (user.status == Status.AKTIF) Color(0xFF16A34A) else Color(0xFFEF4444)
                        val statusBg = if (user.status == Status.AKTIF) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
                        
                        Surface(
                            color = statusBg,
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = user.status.value,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
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
                val isBanned = user.status == Status.BANNED
                OutlinedButton(
                    onClick = onBan,
                    enabled = !isBanLoading && !isDeleteLoading,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, if (isBanned) TealPrimary.copy(alpha = 0.5f) else Color(0xFFFBBF24).copy(alpha = 0.5f))
                ) {
                    if (isBanLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = if (isBanned) TealPrimary else Color(0xFFD97706)
                        )
                    } else {
                        Icon(
                            if (isBanned) Icons.Default.CheckCircle else Icons.Default.Block,
                            contentDescription = null,
                            tint = if (isBanned) TealPrimary else Color(0xFFD97706),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (isBanned) "Aktifkan" else "Ban",
                            color = if (isBanned) TealPrimary else Color(0xFFD97706)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(
                    onClick = onDelete,
                    enabled = !isBanLoading && !isDeleteLoading,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.2f))
                ) {
                    if (isDeleteLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFFEF4444)
                        )
                    } else {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = null,
                            tint = Color(0xFFEF4444),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hapus", color = Color(0xFFEF4444))
                    }
                }
            }
        }
    }
}

@Composable
fun UserInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
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
