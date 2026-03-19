package edu.ucne.vallagest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.vallagest.presentation.navigation.VallaGestNavHost
import edu.ucne.vallagest.ui.theme.VallaGestTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VallaGestTheme {
                val navController = rememberNavController()
                VallaGestNavHost(navController = navController)
            }
        }
    }
}