package edu.ucne.vallagest.presentation.editperfil

sealed interface EditPerfilUiEvent {
    data class NombreChanged(val nombre: String) : EditPerfilUiEvent
    data class EmailChanged(val email: String) : EditPerfilUiEvent
    data class PasswordChanged(val password: String) : EditPerfilUiEvent
    data object Guardar : EditPerfilUiEvent
}