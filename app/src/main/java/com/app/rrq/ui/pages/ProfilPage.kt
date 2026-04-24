package com.app.rrq.ui.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.rrq.ui.theme.*
import com.app.rrq.R

// ═══════════════════════════════════════════════════════════════
// SCREEN — USER PROFILE (Rapli)
// ═══════════════════════════════════════════════════════════════

@Composable
fun UserProfileScreen(
    onNavigate: (Int) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(3) } // Profil aktif (index 3)

    Scaffold(
        containerColor = BackgroundGray,
        bottomBar = {
            UserBottomBar(selected = selectedTab, onSelect = {
                selectedTab = it
                onNavigate(it)
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Teal Header dengan Avatar ─────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(TealPrimary, TealDark)),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(top = 36.dp, bottom = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Avatar dengan inisial
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "R",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Rapli",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Pengguna RoadResQ",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Info Card ─────────────────────────────────────────────
            ProfileInfoCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                items = listOf(
                    ProfileInfoItem(
                        iconRes  = R.drawable.ic_email,
                        label    = "EMAIL",
                        value    = "tbuyut0@gmail.com"
                    ),
                    ProfileInfoItem(
                        iconRes  = R.drawable.ic_phone,
                        label    = "NOMOR TELEPON",
                        value    = "08157234946"
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Menu Card ─────────────────────────────────────────────
            ProfileMenuCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                items = listOf(
                    ProfileMenuItem(
                        iconRes = R.drawable.ic_pen_line,
                        label   = "Edit Profil"
                    ),
                    ProfileMenuItem(
                        iconRes = R.drawable.ic_shield,
                        label   = "Privasi & Keamanan"
                    ),
                    ProfileMenuItem(
                        iconRes = R.drawable.ic_question,
                        label   = "Bantuan"
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Tombol Keluar ─────────────────────────────────────────
            LogoutButton(
                modifier = Modifier.padding(horizontal = 20.dp),
                onClick = onLogout
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ═══════════════════════════════════════════════════════════════
// SCREEN — ADMIN PROFILE (030_ILKOM A_FIKI SULISTIAWAN)
// ═══════════════════════════════════════════════════════════════
@Composable
fun AdminProfileScreen(
    onNavigate: (Int) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(3) } // Profil aktif

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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Teal Header dengan Avatar Admin ───────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(TealPrimary, TealDark)),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(top = 36.dp, bottom = 28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Avatar dengan badge admin
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "0A",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        // Badge shield orange
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(RoundedCornerShape(50))
                                .background(OrangeAccent)
                                .offset(x = 4.dp, y = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter     = painterResource(id = R.drawable.ic_shield),
                                contentDescription = "Admin",
                                modifier    = Modifier.size(14.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "030_ILKOM A_FIKI SULISTIAWAN",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Badge "Administrator"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter     = painterResource(id = R.drawable.ic_shield),
                                contentDescription = null,
                                modifier    = Modifier.size(13.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Administrator",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Info Card (Email + Telepon + Peran) ───────────────────
            ProfileInfoCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                items = listOf(
                    ProfileInfoItem(
                        iconRes = R.drawable.ic_email,
                        label   = "EMAIL",
                        value   = "fikisulistiawan@gmail.com"
                    ),
                    ProfileInfoItem(
                        iconRes = R.drawable.ic_phone,
                        label   = "NOMOR TELEPON",
                        value   = "08256436345"
                    ),
                    ProfileInfoItem(
                        iconRes = R.drawable.ic_shield,
                        label   = "PERAN",
                        value   = "Administrator Sistem"
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Menu Card ─────────────────────────────────────────────
            ProfileMenuCard(
                modifier = Modifier.padding(horizontal = 20.dp),
                items = listOf(
                    ProfileMenuItem(
                        iconRes = R.drawable.ic_pen_line,
                        label   = "Edit Profil"
                    ),
                    ProfileMenuItem(
                        iconRes = R.drawable.ic_chart_column,
                        label   = "Laporan Statistik"
                    ),
                    ProfileMenuItem(
                        iconRes = R.drawable.ic_shield,
                        label   = "Keamanan"
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Tombol Keluar ─────────────────────────────────────────
            LogoutButton(
                modifier = Modifier.padding(horizontal = 20.dp),
                onClick = onLogout
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ─────────────────────────────────────────────
// DATA MODELS
// ─────────────────────────────────────────────

data class ProfileInfoItem(
    @param:DrawableRes val iconRes: Int,
    val label: String,
    val value: String
)

data class ProfileMenuItem(
    @param:DrawableRes val iconRes: Int,
    val label: String,
    val onClick: () -> Unit = {}
)

// ─────────────────────────────────────────────
// SHARED PROFILE COMPONENTS
// ─────────────────────────────────────────────

/** Card berisi baris info (email, telepon, peran) */
@Composable
fun ProfileInfoCard(
    items: List<ProfileInfoItem>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon bulat teal muda
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(50))
                            .background(TealLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter     = painterResource(id = item.iconRes),
                            contentDescription = item.label,
                            modifier    = Modifier.size(18.dp),
                            colorFilter = ColorFilter.tint(TealPrimary)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = item.label,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.value,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    }
                }
                // Divider antar item, kecuali yang terakhir
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier  = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color     = Color(0xFFE5E7EB)
                    )
                }
            }
        }
    }
}

/** Card berisi menu navigasi profil */
@Composable
fun ProfileMenuCard(
    items: List<ProfileMenuItem>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { item.onClick() }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon bulat abu
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(50))
                            .background(BackgroundGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter     = painterResource(id = item.iconRes),
                            contentDescription = item.label,
                            modifier    = Modifier.size(18.dp),
                            colorFilter = ColorFilter.tint(TextPrimary)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text     = item.label,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color    = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter     = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = null,
                        modifier    = Modifier.size(18.dp),
                        colorFilter = ColorFilter.tint(TextSecondary)
                    )
                }
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier  = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color     = Color(0xFFE5E7EB)
                    )
                }
            }
        }
    }
}

/** Tombol Keluar merah dengan border */
@Composable
fun LogoutButton(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    val redColor = Color(0xFFEF4444)

    OutlinedButton(
        onClick  = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape  = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor  = redColor,
            containerColor = Color(0xFFFFF5F5)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, redColor.copy(alpha = 0.4f))
    ) {
        Image(
            painter     = painterResource(id = R.drawable.ic_log_out),
            contentDescription = "Keluar",
            modifier    = Modifier.size(18.dp),
            colorFilter = ColorFilter.tint(redColor)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Keluar",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = redColor
        )
    }
}

// ─────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true, name = "User Profile")
@Composable
fun PreviewUserProfile() {
    MaterialTheme { UserProfileScreen() }
}

@Preview(showBackground = true, showSystemUi = true, name = "Admin Profile")
@Composable
fun PreviewAdminProfile() {
    MaterialTheme { AdminProfileScreen() }
}
