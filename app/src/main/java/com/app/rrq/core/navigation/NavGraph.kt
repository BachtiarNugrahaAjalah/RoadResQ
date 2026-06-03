package com.app.rrq.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.rrq.core.session.SessionManager
import com.app.rrq.view.*
import com.app.rrq.view.auth.*
import com.app.rrq.view.admin.DaftarLaporanScreen
import com.app.rrq.view.admin.KelolaPenggunaScreen
import com.app.rrq.view.admin.VerifikasiLaporanScreen
import com.app.rrq.view.user.BuatLaporanScreen
import com.app.rrq.view.user.DetailLaporanScreen
import com.app.rrq.view.user.RiwayatLaporanScreen

object Routes {
    const val SPLASH = "splash"
    const val HALAMAN_AWAL = "halamanAwal"
    const val LOGIN = "login"
    const val REGISTER = "register"

    // User Routes
    const val USER_DASHBOARD = "user_dashboard"
    const val USER_PROFIL = "user_profil"
    const val USER_BUAT_LAPORAN = "user_buat_laporan"
    const val USER_RIWAYAT = "user_riwayat"
    const val USER_DETAIL_LAPORAN = "user_detail_laporan/{reportId}"

    // Admin Routes
    const val ADMIN_DASHBOARD = "admin_dashboard"
    const val ADMIN_PROFIL = "admin_profil"
    const val ADMIN_KELOLA_PENGGUNA = "admin_kelola_pengguna"
    const val ADMIN_DAFTAR_LAPORAN = "admin_daftar_laporan"
    const val ADMIN_VERIFIKASI_LAPORAN = "admin_verifikasi_laporan/{reportId}"
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
                onLoginSuccess = { isAdmin ->
                    val destination = if (isAdmin) Routes.ADMIN_DASHBOARD else Routes.USER_DASHBOARD
                    navController.navigate(destination) {
                        popUpTo(Routes.HALAMAN_AWAL) { inclusive = true }
                    }
                },
                onBack = {
                    navController.navigate(Routes.HALAMAN_AWAL)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                },
                onRegisterSuccess = {
                    navController.navigate(Routes.USER_DASHBOARD) {
                        popUpTo(Routes.HALAMAN_AWAL) { inclusive = true }
                    }
                },
                onNavigateToAdminDashboard = {
                    navController.navigate(Routes.ADMIN_DASHBOARD)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // --- USER FLOW ---
        composable(Routes.USER_DASHBOARD) {
            UserHomeScreen(
                onNavigate = { index ->
                    handleUserNavigation(index, navController)
                },
                onNavigateToDetail = { reportId ->
                    navController.navigate("user_detail_laporan/$reportId")
                }
            )
        }

        composable(Routes.USER_BUAT_LAPORAN) {
            BuatLaporanScreen(
                onNavigate = { index -> handleUserNavigation(index, navController) },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.USER_RIWAYAT) {
            RiwayatLaporanScreen(
                onNavigate = { index -> handleUserNavigation(index, navController) },
                onNavigateToDetail = { reportId ->
                    navController.navigate("user_detail_laporan/$reportId")
                }
            )
        }

        composable(
            route = Routes.USER_DETAIL_LAPORAN,
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            DetailLaporanScreen(
                reportId = reportId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.USER_PROFIL) {
            UserProfileScreen(
                onNavigate = { index -> handleUserNavigation(index, navController) },
                onLogout = { performLogout(navController) }
            )
        }

        // --- ADMIN FLOW ---
        composable(Routes.ADMIN_DASHBOARD) {
            AdminHomeScreen(onNavigate = { index ->
                handleAdminNavigation(index, navController)
            })
        }

        composable(Routes.ADMIN_DAFTAR_LAPORAN) {
            DaftarLaporanScreen(
                onNavigate = { index -> handleAdminNavigation(index, navController) },
                onNavigateToVerifikasi = { reportId ->
                    navController.navigate("admin_verifikasi_laporan/$reportId")
                }
            )
        }

        composable(
            route = Routes.ADMIN_VERIFIKASI_LAPORAN,
            arguments = listOf(navArgument("reportId") { type = NavType.StringType })
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            VerifikasiLaporanScreen(
                reportId = reportId,
                onNavigate = { index -> handleAdminNavigation(index, navController) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.ADMIN_KELOLA_PENGGUNA) {
            KelolaPenggunaScreen(
                onNavigate = { index -> handleAdminNavigation(index, navController) }
            )
        }

        composable(Routes.ADMIN_PROFIL) {
            AdminProfileScreen(
                onNavigate = { index -> handleAdminNavigation(index, navController) },
                onLogout = { performLogout(navController) }
            )
        }
    }
}

private fun performLogout(navController: NavHostController) {
    SessionManager.clearSession()
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
    route?.let { targetRoute ->
        if (navController.currentDestination?.route != targetRoute) {
            navController.navigate(targetRoute) {
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
    route?.let { targetRoute ->
        if (navController.currentDestination?.route != targetRoute) {
            navController.navigate(targetRoute) {
                popUpTo(Routes.ADMIN_DASHBOARD) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }
}
