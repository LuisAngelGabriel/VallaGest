package edu.ucne.vallagest.domain.usuarios.model

data class Usuario(
    val usuarioId: Int,
    val nombre: String,
    val email: String,
    val rol: String
)