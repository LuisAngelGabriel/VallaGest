package edu.ucne.vallagest.presentation.home

import edu.ucne.vallagest.domain.vallas.model.Valla

sealed interface HomeEvent {
    data object OnRefresh : HomeEvent
    data class OnDeleteValla(val valla: Valla) : HomeEvent
}