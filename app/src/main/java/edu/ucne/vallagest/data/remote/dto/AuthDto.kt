package edu.ucne.vallagest.data.remote.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String = "Usuario"
)

data class AuthResponse(
    val usuarioId: Int,
    val nombre: String,
    val email: String,
    val rol: String
)