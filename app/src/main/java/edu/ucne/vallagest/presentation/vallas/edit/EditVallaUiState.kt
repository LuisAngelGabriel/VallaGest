package edu.ucne.vallagest.presentation.vallas.edit

import edu.ucne.vallagest.domain.categorias.model.Categoria

data class EditVallaUiState(
    val vallaId: Int? = null,
    val nombre: String = "",
    val ubicacion: String = "",
    val precioMensual: String = "",
    val descripcion: String = "",
    val categoriaId: Int = 0,
    val categorias: List<Categoria> = emptyList(),
    val imagenUrl: String? = null,
    val nombreError: String? = null,
    val ubicacionError: String? = null,
    val precioError: String? = null,
    val isSaving: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val error: String? = null
)