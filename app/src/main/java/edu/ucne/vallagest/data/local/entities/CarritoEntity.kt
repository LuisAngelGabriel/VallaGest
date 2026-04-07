package edu.ucne.vallagest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CarritoItems")
data class CarritoEntity(
    @PrimaryKey
    val carritoItemId: Int,
    val vallaId: Int,
    val nombreValla: String,
    val precio: Double,
    val imagenUrl: String?
)