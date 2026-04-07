package edu.ucne.vallagest.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.vallagest.presentation.categorias.edit.CategoriaEditScreen
import edu.ucne.vallagest.presentation.categorias.list.CategoriaListScreen
import edu.ucne.vallagest.presentation.home.HomeScreen
import edu.ucne.vallagest.presentation.home.HomeViewModel
import edu.ucne.vallagest.presentation.login.LoginScreen
import edu.ucne.vallagest.presentation.perfil.PerfilScreen
import edu.ucne.vallagest.presentation.register.RegisterScreen
import edu.ucne.vallagest.presentation.vallas.edit.EditVallaScreen
import edu.ucne.vallagest.presentation.carrito.CarritoScreen

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
                goToValla = { id ->
                    navController.navigate(Screen.VallaEdit(vallaId = id))
                },
                createValla = {
                    navController.navigate(Screen.VallaEdit(vallaId = 0))
                },
                goToPerfil = {
                    navController.navigate(Screen.Perfil)
                },
                goToCategorias = {
                    navController.navigate(Screen.CategoriaList)
                },
                goToCarrito = {
                    navController.navigate(Screen.Carrito)
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
                    navController.navigate(Screen.CategoriaList)
                },
                onNavigateToCarrito = {
                    navController.navigate(Screen.Carrito)
                }
            )
        }

        composable<Screen.CategoriaList> {
            CategoriaListScreen(
                onAdd = { navController.navigate(Screen.CategoriaEdit(0)) },
                onEdit = { id -> navController.navigate(Screen.CategoriaEdit(id)) },
                goToExplorar = { navController.navigate(Screen.Home) },
                goToCarrito = { navController.navigate(Screen.Carrito) },
                goToPerfil = { navController.navigate(Screen.Perfil) }
            )
        }

        composable<Screen.CategoriaEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.CategoriaEdit>()
            CategoriaEditScreen(
                categoriaId = args.categoriaId,
                onBack = { navController.popBackStack() }
            )
        }

        composable<Screen.VallaEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.VallaEdit>()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val usuario by homeViewModel.usuarioLogueado.collectAsStateWithLifecycle()
            val isAdmin = usuario?.rol == "Admin"

            EditVallaScreen(
                vallaId = args.vallaId,
                isAdmin = isAdmin,
                goBack = { navController.popBackStack() }
            )
        }

        composable<Screen.Carrito> {
            CarritoScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}