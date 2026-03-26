package edu.ucne.vallagest.data.remote.dto

import edu.ucne.vallagest.domain.vallas.model.Valla

data class VallaDto(
    val vallaId: Int,
    val nombre: String,
    val descripcion: String?,
    val ubicacion: String,
    val precioMensual: Double,
    val imagenUrl: String?,
    val estaOcupada: Boolean,
    val categoriaId: Int,
    val categoriaNombre: String?
) {
    fun toDomain() = Valla(
        vallaId = vallaId,
        nombre = nombre,
        descripcion = descripcion,
        ubicacion = ubicacion,
        precioMensual = precioMensual,
        imagenUrl = imagenUrl,
        estaOcupada = estaOcupada,
        categoriaId = categoriaId,
        nombreCategoria = categoriaNombre
    )
}