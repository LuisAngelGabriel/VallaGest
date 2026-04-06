package edu.ucne.vallagest.presentation.vallas.edit

sealed interface EditVallaUiEvent {
    data class Load(val id: Int) : EditVallaUiEvent
    data class NombreChanged(val value: String) : EditVallaUiEvent
    data class UbicacionChanged(val value: String) : EditVallaUiEvent
    data class PrecioChanged(val value: String) : EditVallaUiEvent
    data class DescripcionChanged(val value: String) : EditVallaUiEvent
    data class CategoriaChanged(val id: Int) : EditVallaUiEvent
    data class ImagenSeleccionada(val uri: String) : EditVallaUiEvent
    data object Save : EditVallaUiEvent
}