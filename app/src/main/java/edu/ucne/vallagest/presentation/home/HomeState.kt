package edu.ucne.vallagest.presentation.home

import edu.ucne.vallagest.domain.vallas.model.Valla

data class HomeState(
    val isLoading: Boolean = false,
    val vallas: List<Valla> = emptyList(),
    val error: String = ""
)