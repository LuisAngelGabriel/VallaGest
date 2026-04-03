package edu.ucne.vallagest.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.NombreChanged -> _state.update { it.copy(nombre = event.nombre, error = null) }
            is RegisterEvent.EmailChanged -> _state.update { it.copy(email = event.email, error = null) }
            is RegisterEvent.ClaveChanged -> _state.update { it.copy(clave = event.clave, error = null) }
            is RegisterEvent.ConfirmarClaveChanged -> _state.update { it.copy(confirmarClave = event.clave, error = null) }
            is RegisterEvent.OnRegister -> registrar()
        }
    }

    private fun registrar() {
        val s = _state.value

        if (s.nombre.isBlank() || s.email.isBlank() || s.clave.isBlank() || s.confirmarClave.isBlank()) {
            _state.update { it.copy(error = "Todos los campos son obligatorios") }
            return
        }

        if (s.clave != s.confirmarClave) {
            _state.update { it.copy(error = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            authRepository.registro(s.nombre, s.email, s.clave).collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _state.update { it.copy(isLoading = true, error = null) }
                    is Resource.Succes -> _state.update { it.copy(isLoading = false, registrationSuccess = true) }
                    is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }
}