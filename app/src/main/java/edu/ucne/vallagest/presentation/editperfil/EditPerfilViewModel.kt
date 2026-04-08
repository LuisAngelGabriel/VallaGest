package edu.ucne.vallagest.presentation.editperfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.RegisterRequest
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditPerfilUiState())
    val state = _state.asStateFlow()

    private var usuarioId: Int = 0
    private var currentEmail: String = ""
    private var currentRol: String = ""

    init {
        viewModelScope.launch {
            authRepository.getSession().firstOrNull()?.let { usuario ->
                usuarioId = usuario.usuarioId
                currentEmail = usuario.email
                currentRol = usuario.rol
                _state.update { it.copy(nombre = usuario.nombre) }
            }
        }
    }

    fun onNombreChange(nombre: String) {
        _state.update { it.copy(nombre = nombre, nombreError = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChange(password: String) {
        _state.update { it.copy(confirmPassword = password, confirmPasswordError = null) }
    }

    fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun toggleConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    private fun validar(): Boolean {
        var isValid = true
        val nombre = _state.value.nombre
        val pass = _state.value.password
        val confirmPass = _state.value.confirmPassword

        if (nombre.isBlank()) {
            _state.update { it.copy(nombreError = "El nombre no puede estar vacío") }
            isValid = false
        }

        if (pass.isNotEmpty() && pass.length < 6) {
            _state.update { it.copy(passwordError = "La contraseña debe tener al menos 6 caracteres") }
            isValid = false
        }

        if (pass != confirmPass) {
            _state.update { it.copy(confirmPasswordError = "Las contraseñas no coinciden") }
            isValid = false
        }

        return isValid
    }

    fun guardarCambios() {
        if (!validar()) return

        viewModelScope.launch {
            val request = RegisterRequest(
                nombre = _state.value.nombre,
                email = currentEmail,
                password = _state.value.password,
                rol = currentRol
            )

            authRepository.actualizarUsuario(usuarioId, request).collect { result ->
                _state.update {
                    when (result) {
                        is Resource.Loading -> it.copy(isLoading = true, error = null)
                        is Resource.Succes -> it.copy(isLoading = false, isSuccess = true)
                        is Resource.Error -> it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }
}