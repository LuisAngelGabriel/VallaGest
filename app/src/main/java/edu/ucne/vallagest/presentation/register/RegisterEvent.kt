package edu.ucne.vallagest.presentation.register

sealed class RegisterEvent {
    data class NombreChanged(val nombre: String) : RegisterEvent()
    data class EmailChanged(val email: String) : RegisterEvent()
    data class ClaveChanged(val clave: String) : RegisterEvent()
    data class ConfirmarClaveChanged(val clave: String) : RegisterEvent()
    object OnRegister : RegisterEvent()
}