package edu.ucne.vallagest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.vallagest.presentation.home.HomeScreen
import edu.ucne.vallagest.presentation.login.LoginScreen

@Composable
fun VallaGestNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login
    ) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                }
            )
        }

        // Pantalla de Home
        composable<Screen.Home> {
            HomeScreen(
                onDrawer = { },
                goToValla = { /* Navegación de vallas quitada */ },
                createValla = { /* Navegación de vallas quitada */ }
            )
        }
    }
}