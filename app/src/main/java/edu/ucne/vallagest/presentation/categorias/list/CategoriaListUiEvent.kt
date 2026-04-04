package edu.ucne.vallagest.presentation.categorias.list

sealed class CategoriaListUiEvent {
    object Refresh : CategoriaListUiEvent()
    data class Delete(val id: Int) : CategoriaListUiEvent()
}