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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import com.app.rrq.R
import com.app.rrq.data.UserData
import com.app.rrq.model.User
import com.app.rrq.ui.theme.*

@Composable
fun AppRoot(isAdmin: Boolean = false) {
    MaterialTheme {
        if (isAdmin) AdminHomeScreen() else UserHomeScreen()
    }
}

@Composable
fun UserHomeScreen(
    onNavigate: (Int) -> Unit = {}
) {
    var user = UserData.dataUser[1]
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = BackgroundGray,
        contentWindowInsets = WindowInsets(0),
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(TealPrimary, TealDark)),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Halo,",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.name,
                                    color = Color.White,
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "👋", fontSize = 22.sp)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Bantu jalan jadi lebih baik dengan satu laporan.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                        }
                        BellButton(iconRes = R.drawable.ic_bell)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        UserStatCard(label = "Dikirim",  value = "1", color = TealPrimary,  modifier = Modifier.weight(1f))
                        UserStatCard(label = "Diproses", value = "1", color = OrangeAccent, modifier = Modifier.weight(1f))
                        UserStatCard(label = "Selesai",  value = "0", color = GreenAccent,  modifier = Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SectionTitle(title = "Aksi Cepat", modifier = Modifier.padding(horizontal = 20.dp))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    label     = "Buat Laporan",
                    sublabel  = "Lapor kerusakan",
                    iconRes   = R.drawable.ic_add_laporan,
                    isPrimary = true,
                    modifier  = Modifier.weight(1f)
                )
                QuickActionCard(
                    label     = "Riwayat",
                    sublabel  = "Lihat semua",
                    iconRes   = R.drawable.ic_history,
                    isPrimary = false,
                    modifier  = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(title = "Laporan Terbaru")
                Text(
                    text = "Lihat semua",
                    color = TealPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ReportItemCard(
                title        = "Jalan Berlubang",
                subtitle     = "Jl untung suropati",
                leadIconRes  = R.drawable.ic_add_laporan,
                trailIconRes = R.drawable.ic_arrow_right,
                modifier     = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun UserStatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, fontSize = 11.sp, color = TextSecondary, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun QuickActionCard(
    label: String,
    sublabel: String,
    @DrawableRes iconRes: Int,
    isPrimary: Boolean,
    modifier: Modifier = Modifier
) {
    val bgColor   = if (isPrimary) TealPrimary else CardWhite
    val textColor = if (isPrimary) Color.White else TextPrimary
    val subColor  = if (isPrimary) Color.White.copy(alpha = 0.8f) else TextSecondary
    val iconBg    = if (isPrimary) Color.White.copy(alpha = 0.2f) else TealLight
    val iconTint  = if (isPrimary) Color.White else TealPrimary

    Card(
        modifier  = modifier.height(120.dp).clickable { },
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter     = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier    = Modifier.size(22.dp),
                    colorFilter = ColorFilter.tint(iconTint)
                )
            }
            Column {
                Text(text = label,    fontWeight = FontWeight.Bold, color = textColor, fontSize = 14.sp)
                Text(text = sublabel, color = subColor, fontSize = 11.sp)
            }
        }
    }
}

@Composable
fun ReportItemCard(
    title: String,
    subtitle: String,
    @DrawableRes leadIconRes: Int,
    @DrawableRes trailIconRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier.fillMaxWidth().clickable { },
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(TealLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter     = painterResource(id = leadIconRes),
                    contentDescription = null,
                    modifier    = Modifier.size(22.dp),
                    colorFilter = ColorFilter.tint(TealPrimary)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title,    fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TextPrimary)
                Text(text = subtitle, fontSize = 12.sp, color = TextSecondary)
            }
            Image(
                painter     = painterResource(id = trailIconRes),
                contentDescription = null,
                modifier    = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(TextSecondary)
            )
        }
    }
}

@Composable
fun AdminHomeScreen(
    onNavigate: (Int) -> Unit = {}
) {
    var user = UserData.dataUser[2]
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = BackgroundGray,
        contentWindowInsets = WindowInsets(0),
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(listOf(TealPrimary, TealDark)),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 28.dp)
            ) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                "Halo Admin,",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 14.sp
                            )
                            Text(
                                text = user.name,
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Pantau & verifikasi laporan masuk hari ini.",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 13.sp
                            )
                        }
                        BellButton(iconRes = R.drawable.ic_bell)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier  = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape     = RoundedCornerShape(18.dp),
                colors    = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "TOTAL LAPORAN",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextSecondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "3",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = TealPrimary
                    )
                    Text(text = "Semua laporan masuk", fontSize = 13.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatCard(
                        value       = "0",
                        label       = "Laporan Baru",
                        iconBgColor = Color(0xFFFEF3C7),
                        iconTint    = OrangeAccent,
                        iconRes     = R.drawable.ic_schedule,
                        modifier    = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        value       = "3",
                        label       = "Diproses",
                        iconBgColor = TealLight,
                        iconTint    = TealPrimary,
                        iconRes     = R.drawable.ic_document,
                        modifier    = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AdminStatCard(
                        value       = "0",
                        label       = "Selesai",
                        iconBgColor = Color(0xFFDCFCE7),
                        iconTint    = GreenAccent,
                        iconRes     = R.drawable.ic_check,
                        modifier    = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        value       = "1",
                        label       = "Prioritas Tinggi",
                        iconBgColor = Color(0xFFFEE2E2),
                        iconTint    = RedAccent,
                        iconRes     = R.drawable.ic_alert,
                        modifier    = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier  = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable { },
                shape     = RoundedCornerShape(18.dp),
                colors    = CardDefaults.cardColors(containerColor = TealPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter     = painterResource(id = R.drawable.ic_list_checks),
                            contentDescription = "Kelola Laporan",
                            modifier    = Modifier.size(24.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(text = "Kelola Laporan",           color = Color.White,                    fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Verifikasi & atur status", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    value: String,
    label: String,
    iconBgColor: Color,
    iconTint: Color,
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter     = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier    = Modifier.size(22.dp),
                    colorFilter = ColorFilter.tint(iconTint)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = label, fontSize = 12.sp,  color = TextSecondary)
        }
    }
}

@Composable
fun UserBottomBar(selected: Int, onSelect: (Int) -> Unit) {
    val items = listOf(
        Pair("Beranda", R.drawable.ic_home),
        Pair("Lapor",   R.drawable.ic_add_laporan),
        Pair("Riwayat", R.drawable.ic_history),
        Pair("Profil",  R.drawable.ic_user)
    )
    NavigationBar(containerColor = CardWhite, tonalElevation = 4.dp) {
        items.forEachIndexed { idx, (label, iconRes) ->
            val isSelected = selected == idx
            NavigationBarItem(
                selected = isSelected,
                onClick  = { onSelect(idx) },
                icon = {
                    Image(
                        painter     = painterResource(id = iconRes),
                        contentDescription = label,
                        modifier    = Modifier.size(22.dp),
                        colorFilter = ColorFilter.tint(
                            if (isSelected) TealPrimary else TextSecondary
                        )
                    )
                },
                label  = { Text(label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor   = TealPrimary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor      = TealLight
                )
            )
        }
    }
}

@Composable
fun AdminBottomBar(selected: Int, onSelect: (Int) -> Unit) {
    val items = listOf(
        Pair("Beranda",  R.drawable.ic_home),
        Pair("Laporan",  R.drawable.ic_list_checks),
        Pair("Pengguna", R.drawable.ic_users),
        Pair("Profil",   R.drawable.ic_user)
    )
    NavigationBar(containerColor = CardWhite, tonalElevation = 4.dp) {
        items.forEachIndexed { idx, (label, iconRes) ->
            val isSelected = selected == idx
            NavigationBarItem(
                selected = isSelected,
                onClick  = { onSelect(idx) },
                icon = {
                    Image(
                        painter     = painterResource(id = iconRes),
                        contentDescription = label,
                        modifier    = Modifier.size(22.dp),
                        colorFilter = ColorFilter.tint(
                            if (isSelected) TealPrimary else TextSecondary
                        )
                    )
                },
                label  = { Text(label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedTextColor   = TealPrimary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor      = TealLight
                )
            )
        }
    }
}

@Composable
fun BellButton(@DrawableRes iconRes: Int) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter     = painterResource(id = iconRes),
            contentDescription = "Notifikasi",
            modifier    = Modifier.size(22.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text     = title,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold,
        color    = TextPrimary,
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true, name = "User Screen")
@Composable
fun PreviewUserScreen() {
    MaterialTheme { UserHomeScreen() }
}

@Preview(showBackground = true, showSystemUi = true, name = "Admin Screen")
@Composable
fun PreviewAdminScreen() {
    MaterialTheme { AdminHomeScreen() }
}
