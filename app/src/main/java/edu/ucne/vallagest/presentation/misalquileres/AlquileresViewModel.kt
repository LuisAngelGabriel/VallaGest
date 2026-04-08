package edu.ucne.vallagest.presentation.misalquileres

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.ordenes.usecases.GetHistorialUseCase
import edu.ucne.vallagest.domain.ordenes.usecases.RealizarPagoUseCase
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AlquileresViewModel @Inject constructor(
    private val getHistorialUseCase: GetHistorialUseCase,
    private val realizarPagoUseCase: RealizarPagoUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AlquileresUiState())
    val state = _state.asStateFlow()

    fun onEvent(event: AlquileresUiEvent) {
        when (event) {
            is AlquileresUiEvent.CargarAlquileres -> cargarDatos()
            is AlquileresUiEvent.CancelarAlquiler -> cancelar(event.ordenId)
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            val usuario = authRepository.getSession().firstOrNull() ?: return@launch
            getHistorialUseCase(usuario.usuarioId).collect { result ->
                _state.update {
                    when(result) {
                        is Resource.Loading -> it.copy(isLoading = true)
                        is Resource.Succes -> it.copy(isLoading = false, alquileres = result.data ?: emptyList())
                        is Resource.Error -> it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    private fun cancelar(ordenId: Int) {
        viewModelScope.launch {
            realizarPagoUseCase.cancelar(ordenId).collect { result ->
                if (result is Resource.Succes) cargarDatos()
            }
        }
    }
}