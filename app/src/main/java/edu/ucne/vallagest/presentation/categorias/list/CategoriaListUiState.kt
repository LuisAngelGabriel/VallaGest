package edu.ucne.vallagest.presentation.categorias.list

import edu.ucne.vallagest.domain.categorias.model.Categoria

data class CategoriaListUiState(
    val isLoading: Boolean = false,
    val categorias: List<Categoria> = emptyList(),
    val error: String? = null,
    val isAdmin: Boolean = false
)