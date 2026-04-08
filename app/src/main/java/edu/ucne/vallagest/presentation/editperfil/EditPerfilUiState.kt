package edu.ucne.vallagest.presentation.editperfil

data class EditPerfilUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val nombre: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val nombreError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val error: String? = null
)