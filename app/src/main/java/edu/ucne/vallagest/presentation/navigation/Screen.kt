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
}