package edu.ucne.vallagest.presentation.login

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object LoginClick : LoginUiEvent
}