package com.app.rrq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.app.rrq.core.navigation.AppNavigation
import com.app.rrq.core.theme.RoadResQTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            RoadResQTheme {
                AppNavigation()
            }
        }
    }
}
