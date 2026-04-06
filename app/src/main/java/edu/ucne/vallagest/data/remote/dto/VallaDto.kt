package edu.ucne.vallagest.data.remote.dto

data class VallaDto(
    val vallaId: Int,
    val nombre: String,
    val descripcion: String?,
    val ubicacion: String,
    val precioMensual: Double,
    val imagenUrl: String?,
    val estaOcupada: Boolean,
    val categoriaId: Int,
    val nombreCategoria: String?
)