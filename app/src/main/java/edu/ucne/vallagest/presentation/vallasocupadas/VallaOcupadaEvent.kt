package edu.ucne.vallagest.presentation.vallasocupadas

sealed interface VallaOcupadaEvent {
    data object Refresh : VallaOcupadaEvent
}