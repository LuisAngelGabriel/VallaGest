package edu.ucne.vallagest.presentation.ordenes

sealed interface OrdenUiEvent {
    data class OnMesesChange(val meses: Int) : OrdenUiEvent
    data class OnMetodoChange(val metodo: Int) : OrdenUiEvent
    data class Pagar(val totalVallas: Double) : OrdenUiEvent
    data object GetHistorial : OrdenUiEvent
    data object ClearError : OrdenUiEvent
}