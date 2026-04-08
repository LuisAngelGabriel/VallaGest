package edu.ucne.vallagest.data.remote.dto

data class OrdenDto(
    val ordenId: Int,
    val fechaOrden: String,
    val total: Double,
    val metodo: String,
    val estado: String,
    val comprobanteUrl: String?,
    val detalles: List<OrdenDetalleDto>
)

data class OrdenDetalleDto(
    val vallaId: Int,
    val nombreValla: String,
    val precioAplicado: Double,
    val meses: Int
)