package com.app.rrq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.rrq.ui.auth.LoginScreen
import com.app.rrq.ui.auth.RegisterScreen
import com.app.rrq.ui.pages.*
import com.app.rrq.ui.pages.admin.AdminNotifikasiPage
import com.app.rrq.ui.pages.admin.DaftarLaporanPage
import com.app.rrq.ui.pages.admin.KelolaPenggunaPage
import com.app.rrq.ui.pages.admin.VerifikasiLaporanPage
import com.app.rrq.ui.pages.user.BuatLaporanPage
import com.app.rrq.ui.pages.user.DetailLaporanPage
import com.app.rrq.ui.pages.user.NotifikasiPage
import com.app.rrq.ui.pages.user.RiwayatLaporanPage

object Routes {
    const val SPLASH = "splash"
    const val HALAMAN_AWAL = "halamanAwal"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // USER
    const val USER_DASHBOARD = "user_dashboard"
    const val USER_PROFIL = "user_profil"
    const val USER_BUAT_LAPORAN = "user_buat_laporan"
    const val USER_RIWAYAT = "user_riwayat"
    const val USER_NOTIFIKASI = "user_notifikasi"
    const val USER_DETAIL_LAPORAN = "user_detail_laporan/{laporanId}"

    // ADMIN
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_PROFIL = "admin_profil"
    const val ADMIN_KELOLA_PENGGUNA = "admin_kelola_pengguna"
    const val ADMIN_VERIFIKASI_LAPORAN = "admin_verifikasi_laporan/{laporanId}"
    const val ADMIN_DAFTAR_LAPORAN = "admin_daftar_laporan"
    const val ADMIN_NOTIFIKASI = "admin_notifikasi"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(navController)
        }

        composable(Routes.HALAMAN_AWAL) {
            HalamanAwal(navController)
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = { role ->
                    val destination = if (role == "admin")
                        Routes.ADMIN_DASHBOARD
                    else
                        Routes.USER_DASHBOARD

                    navController.navigate(destination) {
                        popUpTo(Routes.HALAMAN_AWAL) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // USER
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    // Berhasil daftar -> Arahkan ke halaman LOGIN
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HALAMAN_AWAL) { inclusive = false }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // USER
        composable(Routes.USER_DASHBOARD) {
            UserHomeScreen(
                onNavigate = { index -> handleUserNavigation(index, navController) },
                onNotifikasi = { navController.navigate(Routes.USER_NOTIFIKASI) },
                onNavigateToDetail = { id -> navController.navigate("user_detail_laporan/$id") }
            )
        }

        composable(Routes.USER_BUAT_LAPORAN) {
            BuatLaporanPage(
                onNavigate = { index -> handleUserNavigation(index, navController) }
            )
        }

        composable(Routes.USER_RIWAYAT) {
            RiwayatLaporanPage(
                onNavigate = { index -> handleUserNavigation(index, navController) },
                onNavigateToDetail = { id -> navController.navigate("user_detail_laporan/$id") }
            )
        }

        composable(Routes.USER_NOTIFIKASI) {
            NotifikasiPage(
                onBack = { navController.popBackStack() },
                onNavigateToDetail = { id -> navController.navigate("user_detail_laporan/$id") }
            )
        }

        composable(
            route = Routes.USER_DETAIL_LAPORAN,
            arguments = listOf(navArgument("laporanId") { type = NavType.StringType })
        ) { backStackEntry ->
            val laporanId = backStackEntry.arguments?.getString("laporanId") ?: ""
            DetailLaporanPage(laporanId = laporanId, onBack = { navController.popBackStack() })
        }

        composable(Routes.USER_PROFIL) {
            UserProfileScreen(
                onNavigate = { index -> handleUserNavigation(index, navController) },
                onLogout = { performLogout(navController) }
            )
        }

        // ADMIN
        composable(Routes.ADMIN_DASHBOARD) {
            AdminHomeScreen(
                onNavigate = { index -> handleAdminNavigation(index, navController) },
                onNotifikasi = { navController.navigate(Routes.ADMIN_NOTIFIKASI) }
            )
        }

        composable(Routes.ADMIN_NOTIFIKASI) {
            AdminNotifikasiPage(
                onBack = { navController.popBackStack() },
                onNavigateToVerifikasi = { id -> navController.navigate("admin_verifikasi_laporan/$id") }
            )
        }

        composable(
            route = Routes.ADMIN_VERIFIKASI_LAPORAN,
            arguments = listOf(navArgument("laporanId") { type = NavType.StringType })
        ) { backStackEntry ->
            val laporanId = backStackEntry.arguments?.getString("laporanId") ?: ""
            VerifikasiLaporanPage(
                laporanId = laporanId,
                onNavigate = { index -> handleAdminNavigation(index, navController) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_KELOLA_PENGGUNA) {
            KelolaPenggunaPage(
                onNavigate = { index -> handleAdminNavigation(index, navController) }
            )
        }

        composable(Routes.ADMIN_PROFIL) {
            AdminProfileScreen(
                onNavigate = { index -> handleAdminNavigation(index, navController) },
                onLogout = { performLogout(navController) }
            )
        }

        composable(Routes.ADMIN_DAFTAR_LAPORAN) {
            DaftarLaporanPage(
                onNavigate = { index -> handleAdminNavigation(index, navController) },
                onNavigateToVerifikasi = { id -> navController.navigate("admin_verifikasi_laporan/$id") }
            )
        }
    }
}

private fun performLogout(navController: NavHostController) {
    navController.navigate(Routes.HALAMAN_AWAL) {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}

private fun handleUserNavigation(index: Int, navController: NavHostController) {
    val route = when (index) {
        0 -> Routes.USER_DASHBOARD
        1 -> Routes.USER_BUAT_LAPORAN
        2 -> Routes.USER_RIWAYAT
        3 -> Routes.USER_PROFIL
        else -> null
    }

    route?.let {
        if (navController.currentDestination?.route != it) {
            navController.navigate(it) {
                popUpTo(Routes.USER_DASHBOARD) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}

private fun handleAdminNavigation(index: Int, navController: NavHostController) {
    val route = when (index) {
        0 -> Routes.ADMIN_DASHBOARD
        1 -> Routes.ADMIN_DAFTAR_LAPORAN
        2 -> Routes.ADMIN_KELOLA_PENGGUNA
        3 -> Routes.ADMIN_PROFIL
        else -> null
    }

    route?.let {
        if (navController.currentDestination?.route != it) {
            navController.navigate(it) {
                popUpTo(Routes.ADMIN_DASHBOARD) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}