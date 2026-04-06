package edu.ucne.vallagest.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    object Login : Screen()

    @Serializable
    object Home : Screen()

    @Serializable
    object Register : Screen()

    @Serializable
    object Perfil : Screen()

    @Serializable
    object CategoriaList : Screen()

    @Serializable
    data class CategoriaEdit(val categoriaId: Int = 0) : Screen()

    @Serializable
    data class VallaEdit(val vallaId: Int = 0) : Screen()
}