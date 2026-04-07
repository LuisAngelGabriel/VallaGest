package edu.ucne.vallagest.domain.carrito.model


data class CarritoItem(
    val carritoItemId: Int,
    val vallaId: Int,
    val nombreValla: String,
    val precio: Double,
    val imagenUrl: String?
)