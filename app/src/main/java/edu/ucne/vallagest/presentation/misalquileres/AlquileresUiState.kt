package edu.ucne.vallagest.presentation.misalquileres

import edu.ucne.vallagest.domain.ordenes.model.Orden

data class AlquileresUiState(
    val isLoading: Boolean = false,
    val alquileres: List<Orden> = emptyList(),
    val error: String? = null
)