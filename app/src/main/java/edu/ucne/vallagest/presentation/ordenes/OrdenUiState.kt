package edu.ucne.vallagest.presentation.ordenes

import edu.ucne.vallagest.domain.ordenes.model.Orden

data class OrdenUiState(
    val isLoading: Boolean = false,
    val ordenes: List<Orden> = emptyList(),
    val error: String? = null,
    val pagoExitoso: Boolean = false,
    val meses: Int = 1,
    val metodoPago: Int = 0,
    val comprobanteUrl: String? = null
)