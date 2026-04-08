package edu.ucne.vallagest.data.mappers

import edu.ucne.vallagest.data.local.entities.OrdenDetalleEntity
import edu.ucne.vallagest.data.local.entities.OrdenEntity
import edu.ucne.vallagest.data.remote.dto.OrdenDetalleDto
import edu.ucne.vallagest.data.remote.dto.OrdenDto
import edu.ucne.vallagest.domain.ordenes.model.Orden
import edu.ucne.vallagest.domain.ordenes.model.OrdenDetalle

fun OrdenDto.toDomain(): Orden {
    return Orden(
        ordenId = this.ordenId,
        usuarioId = 0,
        fechaOrden = this.fechaOrden,
        total = this.total,
        metodoPago = this.metodo,
        estado = this.estado,
        comprobanteUrl = this.comprobanteUrl,
        detalles = this.detalles.map { it.toDomain() }
    )
}

fun OrdenDetalleDto.toDomain(): OrdenDetalle {
    return OrdenDetalle(
        vallaId = this.vallaId,
        nombreValla = this.nombreValla, // Usando nombreValla del DTO
        precioAplicado = this.precioAplicado,
        meses = this.meses
    )
}

fun OrdenDto.toEntity(usuarioId: Int): OrdenEntity {
    return OrdenEntity(
        ordenId = this.ordenId,
        usuarioId = usuarioId,
        fechaOrden = this.fechaOrden,
        total = this.total,
        metodo = this.metodo,
        estado = this.estado,
        comprobanteUrl = this.comprobanteUrl
    )
}

fun OrdenDetalleDto.toEntity(ordenId: Int): OrdenDetalleEntity {
    return OrdenDetalleEntity(
        ordenId = ordenId,
        vallaId = this.vallaId,
        nombreValla = this.nombreValla,
        precioAplicado = this.precioAplicado,
        meses = this.meses
    )
}

fun OrdenEntity.toDomain(detalles: List<OrdenDetalleEntity>): Orden {
    return Orden(
        ordenId = this.ordenId,
        usuarioId = this.usuarioId,
        fechaOrden = this.fechaOrden,
        total = this.total,
        metodoPago = this.metodo,
        estado = this.estado,
        comprobanteUrl = this.comprobanteUrl,
        detalles = detalles.map { it.toDomain() }
    )
}

fun OrdenDetalleEntity.toDomain(): OrdenDetalle {
    return OrdenDetalle(
        vallaId = this.vallaId,
        nombreValla = this.nombreValla,
        precioAplicado = this.precioAplicado,
        meses = this.meses
    )
}