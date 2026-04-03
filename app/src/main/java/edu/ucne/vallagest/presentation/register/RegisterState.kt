package edu.ucne.vallagest.presentation.register

data class RegisterState(
    val nombre: String = "",
    val email: String = "",
    val clave: String = "",
    val confirmarClave: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val registrationSuccess: Boolean = false
)