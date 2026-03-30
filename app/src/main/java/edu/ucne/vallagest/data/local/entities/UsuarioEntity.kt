package edu.ucne.vallagest.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Usuarios")
data class UsuarioEntity(
    @PrimaryKey
    val usuarioId: Int,
    val nombre: String,
    val email: String,
    val rol: String,
    val estaLogueado: Boolean = false
)