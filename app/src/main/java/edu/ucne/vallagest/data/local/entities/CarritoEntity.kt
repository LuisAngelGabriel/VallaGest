package edu.ucne.vallagest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CarritoItems")
data class CarritoEntity(
    @PrimaryKey(autoGenerate = true)
    val carritoItemId: Int = 0,
    val vallaId: Int,
    val nombreValla: String,
    val precio: Double,
    val imagenUrl: String?,
    val isSynced: Boolean = true
)