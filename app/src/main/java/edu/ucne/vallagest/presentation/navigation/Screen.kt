package edu.ucne.vallagest.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Login : Screen()

    @Serializable
    data object Home : Screen()
}