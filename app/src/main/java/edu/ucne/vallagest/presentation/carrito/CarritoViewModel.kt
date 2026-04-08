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
        observarCarrito()
    }

    fun onEvent(event: CarritoUiEvent) {
        when (event) {
            is CarritoUiEvent.LoadCarrito -> observarCarrito()
            is CarritoUiEvent.EliminarItem -> eliminarItem(event.id)
        }
    }

    private fun observarCarrito() {
        viewModelScope.launch {
            authRepository.getSession().filterNotNull().flatMapLatest { usuario ->
                getCarritoUseCase(usuario.usuarioId)
            }.collect { result ->
                when (result) {
                    is Resource.Succes -> {
                        val lista = result.data ?: emptyList()
                        _state.update { it.copy(
                            items = lista,
                            isLoading = false,
                            total = lista.sumOf { it.precio }
                        )}
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(error = result.message, isLoading = false) }
                    }
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = _state.value.items.isEmpty()) }
                    }
                }
            }
        }
    }

    private fun eliminarItem(id: Int) {
        viewModelScope.launch {
            deleteCarritoUseCase(id).collectLatest { result ->
                if (result is Resource.Error) {
                    _state.update { it.copy(error = result.message) }
                }
            }
        }
    }
}