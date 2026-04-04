package edu.ucne.vallagest.presentation.categorias.edit

sealed class CategoriaEditUiEvent {
    data class NombreChanged(val nombre: String) : CategoriaEditUiEvent()
    data class DescripcionChanged(val descripcion: String) : CategoriaEditUiEvent()
    object Save : CategoriaEditUiEvent()
}