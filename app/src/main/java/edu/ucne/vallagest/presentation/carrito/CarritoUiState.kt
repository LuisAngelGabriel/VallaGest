package edu.ucne.vallagest.presentation.carrito

import edu.ucne.vallagest.domain.carrito.model.CarritoItem


data class CarritoUiState(
    val items: List<CarritoItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val total: Double = 0.0
)