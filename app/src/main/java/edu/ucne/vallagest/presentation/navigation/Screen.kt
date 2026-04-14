package edu.ucne.vallagest.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Register : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object CategoriaList : Screen()

    @Serializable
    data class CategoriaEdit(val categoriaId: Int = 0) : Screen()

    @Serializable
    data class VallaEdit(val vallaId: Int = 0) : Screen()

    @Serializable
    data object Carrito : Screen()

    @Serializable
    data class Checkout(val total: Double) : Screen()

    @Serializable data class PagoTarjeta(val total: Double, val meses: Int) : Screen()
    @Serializable data class PagoTransferencia(val total: Double, val meses: Int) : Screen()
    @Serializable
    data object Perfil : Screen()

    @Serializable
    data object Alquileres : Screen()

    @Serializable
    data object EditarPerfil : Screen()
    @Serializable
    data object VallasOcupadas : Screen()
}