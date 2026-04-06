package edu.ucne.vallagest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.ucne.vallagest.domain.vallas.model.Valla

@Entity(tableName = "Vallas")
data class VallaEntity(
    @PrimaryKey
    val vallaId: Int,
    val nombre: String,
    val descripcion: String?,
    val ubicacion: String,
    val precioMensual: Double,
    val imagenUrl: String?,
    val estaOcupada: Boolean,
    val categoriaId: Int,
    val nombreCategoria: String?
)
