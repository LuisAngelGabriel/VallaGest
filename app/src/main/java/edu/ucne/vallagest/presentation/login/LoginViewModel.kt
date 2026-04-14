package edu.ucne.vallagest.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.usuarios.usecases.LoginUseCase
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email, error = null) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _state.update { it.copy(password = event.password, error = null) }
            }
            LoginUiEvent.LoginClick -> {
                login()
            }
        }
    }

    private fun login() {
        val email = _state.value.email
        val password = _state.value.password

        if (email.isBlank()) {
            _state.update { it.copy(error = "El correo es requerido") }
            return
        }

        if (password.isBlank()) {
            _state.update { it.copy(error = "La contraseña es requerida") }
            return
        }

        loginUseCase(email, password).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true, error = null, success = null) }
                }
                is Resource.Succes -> {
                    _state.update { it.copy(isLoading = false, success = result.data) }
                }
                is Resource.Error -> {
                    val errorMessage = when {
                        result.message?.contains("contraseña", ignoreCase = true) == true -> "Contraseña incorrecta"
                        result.message?.contains("correo", ignoreCase = true) == true -> "Correo incorrecto"
                        result.message?.contains("usuario", ignoreCase = true) == true -> "Usuario no encontrado"
                        else -> result.message ?: "Error al iniciar sesión"
                    }
                    _state.update { it.copy(isLoading = false, error = errorMessage, success = null) }
                }
            }
        }.launchIn(viewModelScope)
    }
}