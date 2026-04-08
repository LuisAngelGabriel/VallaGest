package edu.ucne.vallagest.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CarritoPostDto
import edu.ucne.vallagest.domain.carrito.usecases.AddCarritoUseCase
import edu.ucne.vallagest.domain.carrito.usecases.GetCarritoUseCase
import edu.ucne.vallagest.domain.usuarios.model.Usuario
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import edu.ucne.vallagest.domain.vallas.usecases.DeleteVallaUseCase
import edu.ucne.vallagest.domain.vallas.usecases.GetVallasUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getVallasUseCase: GetVallasUseCase,
    private val deleteVallaUseCase: DeleteVallaUseCase,
    private val addCarritoUseCase: AddCarritoUseCase,
    private val getCarritoUseCase: GetCarritoUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val usuarioLogueado: StateFlow<Usuario?> = authRepository.getSession()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        observeVallas()
        observeCarritoCount()
    }

    fun observeVallas() {
        viewModelScope.launch {
            getVallasUseCase().collect { result ->
                if (result is Resource.Succes) {
                    _state.update { it.copy(isLoading = false, vallas = result.data ?: emptyList()) }
                }
            }
        }
    }

    fun observeCarritoCount() {
        viewModelScope.launch {
            usuarioLogueado.filterNotNull().collectLatest { usuario ->
                getCarritoUseCase(usuario.usuarioId).collectLatest { result ->
                    if (result is Resource.Succes) {
                        _state.update { it.copy(carritoCount = result.data?.size ?: 0) }
                    }
                }
            }
        }
    }

    fun onAgregarAlCarrito(vallaId: Int) {
        viewModelScope.launch {
            val usuario = usuarioLogueado.value ?: return@launch
            val item = CarritoPostDto(usuarioId = usuario.usuarioId, vallaId = vallaId)
            addCarritoUseCase(item).collectLatest { }
        }
    }

    fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteVallaUseCase(id).collectLatest { result ->
                if (result is Resource.Succes) observeVallas()
            }
        }
    }
}