package com.app.rrq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.app.rrq.navigation.AppNavHost
import com.app.rrq.ui.theme.RoadResQTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoadResQTheme {
                val navController = rememberNavController()
                AppNavHost(navController)
            }
        }
    }
}
