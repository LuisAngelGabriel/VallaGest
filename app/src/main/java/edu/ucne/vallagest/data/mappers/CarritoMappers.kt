package edu.ucne.vallagest.data.mappers

import edu.ucne.vallagest.data.local.entities.CarritoEntity
import edu.ucne.vallagest.data.remote.dto.CarritoItemDto
import edu.ucne.vallagest.domain.carrito.model.CarritoItem

fun CarritoItemDto.toEntity(): CarritoEntity {
    return CarritoEntity(
        carritoItemId = this.carritoItemId,
        vallaId = this.vallaId,
        nombreValla = this.nombreValla,
        precio = this.precio,
        imagenUrl = this.imagenUrl
    )
}

fun CarritoEntity.toDomain(): CarritoItem {
    return CarritoItem(
        carritoItemId = this.carritoItemId,
        vallaId = this.vallaId,
        nombreValla = this.nombreValla,
        precio = this.precio,
        imagenUrl = this.imagenUrl
    )
}

fun CarritoItemDto.toDomain(): CarritoItem {
    return CarritoItem(
        carritoItemId = this.carritoItemId,
        vallaId = this.vallaId,
        nombreValla = this.nombreValla,
        precio = this.precio,
        imagenUrl = this.imagenUrl
    )
}