package edu.ucne.vallagest.data.mappers

import edu.ucne.vallagest.data.local.entities.CategoriaEntity
import edu.ucne.vallagest.data.remote.dto.CategoriaDto
import edu.ucne.vallagest.domain.categorias.model.Categoria

fun CategoriaDto.toEntity() = CategoriaEntity(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    rol = rol
)

fun CategoriaEntity.toDomain() = Categoria(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    rol = rol
)

fun Categoria.toDto() = CategoriaDto(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    rol = rol
)

fun CategoriaDto.toDomain() = Categoria(
    categoriaId = categoriaId,
    nombre = nombre,
    descripcion = descripcion,
    rol = rol
)