package edu.ucne.vallagest.domain.vallas.model

data class VallaOcupada(
    val vallaId: Int,
    val nombreValla: String,
    val cliente: String,
    val desde: String,
    val hasta: String,
    val precio: Double,
    val meses: Int
)