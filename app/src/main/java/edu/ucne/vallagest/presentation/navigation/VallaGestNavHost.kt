package edu.ucne.vallagest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.vallagest.presentation.home.HomeScreen
import edu.ucne.vallagest.presentation.login.LoginScreen
import edu.ucne.vallagest.presentation.perfil.PerfilScreen
import edu.ucne.vallagest.presentation.register.RegisterScreen

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
                    navController.navigate(Screen.Register)
                }
            )
        }

        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home) {
                        popUpTo(Screen.Register) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                onDrawer = { },
                goToValla = { },
                createValla = { },
                goToPerfil = {
                    navController.navigate(Screen.Perfil)
                }
            )
        }

        composable<Screen.Perfil> {
            PerfilScreen(
                onLogout = {
                    navController.navigate(Screen.Login) {
                        popUpTo(0)
                    }
                },
                onNavigateToExplorar = {
                    navController.navigate(Screen.Home)
                },
                onNavigateToCategorias = {
                    navController.navigate(Screen.Home)
                },
                onNavigateToCarrito = {
                    navController.navigate(Screen.Home)
                }
            )
        }
    }
}