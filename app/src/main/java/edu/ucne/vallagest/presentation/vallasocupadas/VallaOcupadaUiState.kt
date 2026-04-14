package edu.ucne.vallagest.presentation.vallasocupadas

import edu.ucne.vallagest.domain.vallas.model.VallaOcupada

data class VallaOcupadaUiState(
    val isLoading: Boolean = false,
    val vallas: List<VallaOcupada> = emptyList(),
    val error: String? = null
)