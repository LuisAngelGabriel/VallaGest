package edu.ucne.vallagest.domain.vallas.model

data class Valla(
    val vallaId: Int,
    val nombre: String,
    val descripcion: String?,
    val precioMensual: Double,
    val imagenUrl: String?,
    val estaOcupada: Boolean,
    val categoriaId: Int,
    val nombreCategoria: String? = null
)