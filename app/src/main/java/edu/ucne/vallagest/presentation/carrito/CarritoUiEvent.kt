package edu.ucne.vallagest.presentation.carrito

sealed class CarritoUiEvent {
    data object LoadCarrito : CarritoUiEvent()
    data class EliminarItem(val id: Int) : CarritoUiEvent()
}