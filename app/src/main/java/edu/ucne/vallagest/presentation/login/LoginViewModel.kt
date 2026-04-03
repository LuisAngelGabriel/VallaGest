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

        if (email.isBlank() || password.isBlank()) {
            _state.update { it.copy(error = "Debes completar todos los campos") }
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
                    _state.update { it.copy(isLoading = false, error = result.message, success = null) }
                }
            }
        }.launchIn(viewModelScope)
    }
}