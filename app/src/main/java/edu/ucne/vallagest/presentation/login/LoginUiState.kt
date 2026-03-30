package edu.ucne.vallagest.presentation.login

import edu.ucne.vallagest.domain.usuarios.model.Usuario

data class LoginUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val success: Usuario? = null
)