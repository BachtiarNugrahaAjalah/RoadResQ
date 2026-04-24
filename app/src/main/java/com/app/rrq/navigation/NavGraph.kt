package com.app.rrq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.rrq.DetailLaporan
import com.app.rrq.HalamanAwal
import com.app.rrq.SplashScreen

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
        composable("detailLaporan"){
            DetailLaporan(navController)
        }
    }
}