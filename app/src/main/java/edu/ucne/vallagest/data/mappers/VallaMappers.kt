package edu.ucne.vallagest.data.mappers

import edu.ucne.vallagest.data.local.entities.VallaEntity
import edu.ucne.vallagest.data.local.entities.VallaOcupadaEntity
import edu.ucne.vallagest.data.remote.dto.VallaDto
import edu.ucne.vallagest.data.remote.dto.VallaOcupadaDto
import edu.ucne.vallagest.domain.vallas.model.Valla
import edu.ucne.vallagest.domain.vallas.model.VallaOcupada

fun VallaDto.toEntity() = VallaEntity(
    vallaId = vallaId,
    nombre = nombre,
    descripcion = descripcion,
    ubicacion = ubicacion,
    precioMensual = precioMensual,
    imagenUrl = imagenUrl,
    estaOcupada = estaOcupada,
    categoriaId = categoriaId,
    nombreCategoria = nombreCategoria,
    isSynced = true
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
    nombreCategoria = nombreCategoria,
    isSynced = isSynced
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

fun Valla.toEntity() = VallaEntity(
    vallaId = vallaId,
    nombre = nombre,
    descripcion = descripcion,
    ubicacion = ubicacion,
    precioMensual = precioMensual,
    imagenUrl = imagenUrl,
    estaOcupada = estaOcupada,
    categoriaId = categoriaId,
    nombreCategoria = nombreCategoria,
    isSynced = isSynced
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
    nombreCategoria = nombreCategoria,
    isSynced = true
)

fun VallaOcupadaDto.toEntity() = VallaOcupadaEntity(
    vallaId = vallaId,
    nombreValla = nombreValla,
    cliente = cliente,
    fechaAlquiler = fechaAlquiler,
    fechaVencimiento = fechaVencimiento,
    precio = precio,
    mesesAlquilados = mesesAlquilados
)

fun VallaOcupadaEntity.toDomain() = VallaOcupada(
    vallaId = vallaId,
    nombreValla = nombreValla,
    cliente = cliente,
    desde = fechaAlquiler,
    hasta = fechaVencimiento,
    precio = precio,
    meses = mesesAlquilados
)

fun VallaOcupadaDto.toDomain() = VallaOcupada(
    vallaId = vallaId,
    nombreValla = nombreValla,
    cliente = cliente,
    desde = fechaAlquiler,
    hasta = fechaVencimiento,
    precio = precio,
    meses = mesesAlquilados
)