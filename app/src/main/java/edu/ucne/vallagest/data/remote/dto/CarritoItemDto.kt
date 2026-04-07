package edu.ucne.vallagest.data.remote.dto

data class CarritoItemDto(
    val carritoItemId: Int,
    val vallaId: Int,
    val nombreValla: String,
    val precio: Double,
    val imagenUrl: String?
)

data class CarritoPostDto(
    val usuarioId: Int,
    val vallaId: Int
)