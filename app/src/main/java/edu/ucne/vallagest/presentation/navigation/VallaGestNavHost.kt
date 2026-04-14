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
import edu.ucne.vallagest.presentation.ordenes.CheckoutScreen
import edu.ucne.vallagest.presentation.pago.PagoTarjetaScreen
import edu.ucne.vallagest.presentation.pago.PagoTransferenciaScreen
import edu.ucne.vallagest.presentation.misalquileres.AlquileresScreen
import edu.ucne.vallagest.presentation.editperfil.EditPerfilScreen
import edu.ucne.vallagest.presentation.vallasocupadas.VallaOcupadaScreen

@Composable
fun VallaGestNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login) {
        composable<Screen.Login> {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.Home) { popUpTo(Screen.Login) { inclusive = true } } },
                onNavigateToRegister = { navController.navigate(Screen.Register) }
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.Home) { popUpTo(Screen.Register) { inclusive = true } } },
                onBackToLogin = { navController.popBackStack() }
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                goToValla = { id -> navController.navigate(Screen.VallaEdit(id)) },
                createValla = { navController.navigate(Screen.VallaEdit(0)) },
                goToPerfil = { navController.navigate(Screen.Perfil) },
                goToCategorias = { navController.navigate(Screen.CategoriaList) },
                goToCarrito = { navController.navigate(Screen.Carrito) }
            )
        }
        composable<Screen.Perfil> {
            PerfilScreen(
                onLogout = { navController.navigate(Screen.Login) { popUpTo(0) } },
                onNavigateToExplorar = { navController.navigate(Screen.Home) },
                onNavigateToCategorias = { navController.navigate(Screen.CategoriaList) },
                onNavigateToCarrito = { navController.navigate(Screen.Carrito) },
                onNavigateToAlquileres = { navController.navigate(Screen.Alquileres) },
                onNavigateToEditarPerfil = { navController.navigate(Screen.EditarPerfil) },
                onNavigateToVallasOcupadas = { navController.navigate(Screen.VallasOcupadas) }
            )
        }
        composable<Screen.EditarPerfil> {
            EditPerfilScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.Alquileres> {
            AlquileresScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Screen.VallasOcupadas> {
            VallaOcupadaScreen(
                onNavigateBack = { navController.popBackStack() }
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
            CategoriaEditScreen(categoriaId = args.categoriaId, onBack = { navController.popBackStack() })
        }
        composable<Screen.VallaEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.VallaEdit>()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val usuario by homeViewModel.usuarioLogueado.collectAsStateWithLifecycle()
            EditVallaScreen(vallaId = args.vallaId, isAdmin = usuario?.rol == "Admin", goBack = { navController.popBackStack() })
        }
        composable<Screen.Carrito> {
            CarritoScreen(onBack = { navController.popBackStack() }, onIrAPagar = { total -> navController.navigate(Screen.Checkout(total)) })
        }
        composable<Screen.Checkout> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.Checkout>()
            CheckoutScreen(
                totalVallas = args.total,
                onBack = { navController.popBackStack() },
                onConfirmarMetodo = { totalFinal, metodo, meses ->
                    if (metodo == 0) navController.navigate(Screen.PagoTarjeta(totalFinal, meses))
                    else navController.navigate(Screen.PagoTransferencia(totalFinal, meses))
                }
            )
        }
        composable<Screen.PagoTarjeta> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.PagoTarjeta>()
            PagoTarjetaScreen(
                total = args.total,
                meses = args.meses,
                onBack = { navController.popBackStack() },
                onPagoExitoso = {
                    navController.navigate(Screen.Home) { popUpTo(Screen.Home) { inclusive = true } }
                }
            )
        }
        composable<Screen.PagoTransferencia> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.PagoTransferencia>()
            PagoTransferenciaScreen(
                total = args.total,
                meses = args.meses,
                onBack = { navController.popBackStack() },
                onPagoExitoso = {
                    navController.navigate(Screen.Home) { popUpTo(Screen.Home) { inclusive = true } }
                }
            )
        }
    }
}