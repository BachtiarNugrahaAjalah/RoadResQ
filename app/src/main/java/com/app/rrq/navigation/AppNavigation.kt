package com.app.rrq.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Gunakan AppNavHost dari NavGraph.kt sebagai satu-satunya pengatur navigasi
    AppNavHost(navController = navController)
}