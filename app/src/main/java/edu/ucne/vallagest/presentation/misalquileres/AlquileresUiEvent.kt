package edu.ucne.vallagest.presentation.misalquileres

sealed interface AlquileresUiEvent {
    data object CargarAlquileres : AlquileresUiEvent
    data class CancelarAlquiler(val ordenId: Int) : AlquileresUiEvent
}