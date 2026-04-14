package edu.ucne.vallagest.data.mappers

import edu.ucne.vallagest.data.local.entities.CategoriaEntity
import edu.ucne.vallagest.data.remote.dto.CategoriaDto
import edu.ucne.vallagest.domain.categorias.model.Categoria

fun CategoriaDto.toEntity() = CategoriaEntity(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    isSynced = true
)

fun CategoriaEntity.toDomain() = Categoria(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    isSynced = isSynced
)

fun Categoria.toDto() = CategoriaDto(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion
)

fun Categoria.toEntity() = CategoriaEntity(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    isSynced = isSynced
)

fun CategoriaDto.toDomain() = Categoria(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    isSynced = true
)