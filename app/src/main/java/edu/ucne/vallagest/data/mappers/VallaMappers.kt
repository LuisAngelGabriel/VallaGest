package edu.ucne.vallagest.data.mappers

import edu.ucne.vallagest.data.local.entities.VallaEntity
import edu.ucne.vallagest.data.remote.dto.VallaDto
import edu.ucne.vallagest.domain.vallas.model.Valla

fun VallaDto.toEntity() = VallaEntity(
    vallaId = vallaId,
    nombre = nombre,
    descripcion = descripcion,
    ubicacion = ubicacion,
    precioMensual = precioMensual,
    imagenUrl = imagenUrl,
    estaOcupada = estaOcupada,
    categoriaId = categoriaId,
    nombreCategoria = nombreCategoria
)

fun VallaEntity.toDomain() = Valla(
    vallaId = vallaId,
    nombre = nombre,
    descripcion = descripcion,
    ubicacion = ubicacion,
    precioMensual = precioMensual,
    imagenUrl = imagenUrl,
    estaOcupada = estaOcupada,
    categoriaId = categoriaId,
    nombreCategoria = nombreCategoria
)

fun Valla.toDto() = VallaDto(
    vallaId = vallaId,
    nombre = nombre,
    descripcion = descripcion,
    ubicacion = ubicacion,
    precioMensual = precioMensual,
    imagenUrl = imagenUrl,
    estaOcupada = estaOcupada,
    categoriaId = categoriaId,
    nombreCategoria = nombreCategoria
)

fun VallaDto.toDomain() = Valla(
    vallaId = vallaId,
    nombre = nombre,
    descripcion = descripcion,
    ubicacion = ubicacion,
    precioMensual = precioMensual,
    imagenUrl = imagenUrl,
    estaOcupada = estaOcupada,
    categoriaId = categoriaId,
    nombreCategoria = nombreCategoria
)