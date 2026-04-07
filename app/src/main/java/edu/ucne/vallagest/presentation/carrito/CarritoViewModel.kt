package edu.ucne.vallagest.presentation.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.carrito.usecases.DeleteCarritoUseCase
import edu.ucne.vallagest.domain.carrito.usecases.GetCarritoUseCase
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val getCarritoUseCase: GetCarritoUseCase,
    private val deleteCarritoUseCase: DeleteCarritoUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CarritoUiState())
    val state = _state.asStateFlow()

    init {
        onEvent(CarritoUiEvent.LoadCarrito)
    }

    fun onEvent(event: CarritoUiEvent) {
        when (event) {
            is CarritoUiEvent.LoadCarrito -> cargarCarrito()
            is CarritoUiEvent.EliminarItem -> eliminarItem(event.id)
        }
    }

    private fun cargarCarrito() {
        viewModelScope.launch {
            val user = authRepository.getSession().firstOrNull() ?: return@launch
            getCarritoUseCase(user.usuarioId).collectLatest { result ->
                when (result) {
                    is Resource.Succes -> {
                        val lista = result.data ?: emptyList()
                        _state.update { it.copy(
                            items = lista,
                            isLoading = false,
                            total = lista.sumOf { it.precio }
                        )}
                    }
                    is Resource.Error -> _state.update { it.copy(error = result.message, isLoading = false) }
                    is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun eliminarItem(id: Int) {
        viewModelScope.launch {
            deleteCarritoUseCase(id).collectLatest { result ->
                if (result is Resource.Succes) {
                    cargarCarrito()
                }
            }
        }
    }
}