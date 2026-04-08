package edu.ucne.vallagest.domain.ordenes.model

data class OrdenDetalle(
    val vallaId: Int,
    val nombreValla: String,
    val precioAplicado: Double,
    val meses: Int
)