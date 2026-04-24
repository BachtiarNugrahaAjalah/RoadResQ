package com.app.rrq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.rrq.HalamanAwal
import com.app.rrq.SplashScreen
import com.app.rrq.ui.pages.DaftarLaporanPage
import com.app.rrq.ui.pages.KelolaPenggunaPage

@Composable
fun AppNavHost(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = "splash"
    ){
        composable("splash"){
            SplashScreen(navController)
        }
        composable("halamanAwal"){
            HalamanAwal(navController)
        }
        composable("daftarLaporan") {
            DaftarLaporanPage(navController)
        }
        composable("kelolaPengguna") {
            KelolaPenggunaPage(navController)
        }
    }
}
