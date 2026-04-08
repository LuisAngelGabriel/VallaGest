package edu.ucne.vallagest.domain.ordenes.model

data class Orden(
    val ordenId: Int,
    val usuarioId: Int,
    val fechaOrden: String,
    val total: Double,
    val metodoPago: String,
    val estado: String,
    val comprobanteUrl: String?,
    val detalles: List<OrdenDetalle> = emptyList()
)