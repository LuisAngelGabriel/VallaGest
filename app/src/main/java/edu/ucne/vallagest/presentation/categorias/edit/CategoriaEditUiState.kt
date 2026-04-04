package edu.ucne.vallagest.presentation.categorias.edit

data class CategoriaEditUiState(
    val isLoading: Boolean = false,
    val categoriaId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val error: String? = null,
    val isSuccess: Boolean = false,
    val nombreError: String? = null
)