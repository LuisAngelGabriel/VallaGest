package edu.ucne.vallagest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categorias")
data class CategoriaEntity(
    @PrimaryKey
    val categoriaId: Int,
    val nombre: String,
    val descripcion: String,
    val rol: String? = null
)