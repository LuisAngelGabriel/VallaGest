package edu.ucne.vallagest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "VallasOcupadas")
data class VallaOcupadaEntity(
    @PrimaryKey
    val vallaId: Int,
    val nombreValla: String,
    val cliente: String,
    val fechaAlquiler: String,
    val fechaVencimiento: String,
    val precio: Double,
    val mesesAlquilados: Int
)