package edu.ucne.vallagest.data.remote.dto

data class VallaOcupadaDto(
    val vallaId: Int,
    val nombreValla: String,
    val cliente: String,
    val fechaAlquiler: String,
    val fechaVencimiento: String,
    val precio: Double,
    val mesesAlquilados: Int
)