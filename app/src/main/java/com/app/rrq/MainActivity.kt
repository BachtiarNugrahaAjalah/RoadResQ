package com.app.rrq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.rrq.ui.pages.*
import com.app.rrq.ui.theme.BackgroundGray
import com.app.rrq.ui.theme.RoadResQTheme

object Routes {
    // User
    const val USER_HOME    = "user_home"
    const val USER_LAPOR   = "user_lapor"
    const val USER_RIWAYAT = "user_riwayat"
    const val USER_PROFIL  = "user_profil"

    // Admin
    const val ADMIN_HOME     = "admin_home"
    const val ADMIN_LAPORAN  = "admin_laporan"
    const val ADMIN_PENGGUNA = "admin_pengguna"
    const val ADMIN_PROFIL   = "admin_profil"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoadResQTheme {
                // Ubah isAdmin ke true untuk melihat tampilan Admin
                AppRoot(isAdmin = false)
            }
        }
    }
}

@Composable
fun AppRoot(isAdmin: Boolean = false) {
    val navController = rememberNavController()
    
    // User Routes mapping
    val userTabRoutes = listOf(
        Routes.USER_HOME,
        Routes.USER_LAPOR,
        Routes.USER_RIWAYAT,
        Routes.USER_PROFIL
    )
    
    // Admin Routes mapping
    val adminTabRoutes = listOf(
        Routes.ADMIN_HOME,
        Routes.ADMIN_LAPORAN,
        Routes.ADMIN_PENGGUNA,
        Routes.ADMIN_PROFIL
    )

    val currentTabRoutes = if (isAdmin) adminTabRoutes else userTabRoutes
    val startRoute = if (isAdmin) Routes.ADMIN_HOME else Routes.USER_HOME

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        // --- USER ROUTES ---
        composable(Routes.USER_HOME) {
            UserHomeScreen(onNavigate = { index ->
                navController.navigate(userTabRoutes[index]) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
        composable(Routes.USER_LAPOR) {
            PlaceholderScreen("Buat Laporan", isAdmin, userTabRoutes, adminTabRoutes, navController)
        }
        composable(Routes.USER_RIWAYAT) {
            PlaceholderScreen("Riwayat Laporan", isAdmin, userTabRoutes, adminTabRoutes, navController)
        }
        composable(Routes.USER_PROFIL) {
            UserProfileScreen(onNavigate = { index ->
                navController.navigate(userTabRoutes[index]) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }

        // --- ADMIN ROUTES ---
        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(onNavigate = { index ->
                navController.navigate(adminTabRoutes[index]) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
        composable(Routes.ADMIN_LAPORAN) {
            PlaceholderScreen("Kelola Laporan", isAdmin, userTabRoutes, adminTabRoutes, navController)
        }
        composable(Routes.ADMIN_PENGGUNA) {
            PlaceholderScreen("Kelola Pengguna", isAdmin, userTabRoutes, adminTabRoutes, navController)
        }
        composable(Routes.ADMIN_PROFIL) {
            AdminProfileScreen(onNavigate = { index ->
                navController.navigate(adminTabRoutes[index]) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
    }
}

@Composable
fun PlaceholderScreen(
    title: String, 
    isAdmin: Boolean,
    userRoutes: List<String>,
    adminRoutes: List<String>,
    navController: androidx.navigation.NavController
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val currentTabRoutes = if (isAdmin) adminRoutes else userRoutes
    val selectedTab = currentTabRoutes.indexOf(currentRoute).coerceAtLeast(0)

    Scaffold(
        bottomBar = {
            if (isAdmin) {
                AdminBottomBar(selected = selectedTab, onSelect = { index ->
                    navController.navigate(adminRoutes[index]) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            } else {
                UserBottomBar(selected = selectedTab, onSelect = { index ->
                    navController.navigate(userRoutes[index]) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Halaman $title\n(Segera Hadir)",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "User Screen")
@Composable
fun PreviewUserScreen() {
    MaterialTheme { AppRoot(true) }
}