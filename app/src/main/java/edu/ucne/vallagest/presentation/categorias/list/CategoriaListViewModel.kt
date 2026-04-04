package edu.ucne.vallagest.presentation.categorias.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.categorias.usecases.DeleteCategoriaUseCase
import edu.ucne.vallagest.domain.categorias.usecases.GetCategoriasUseCase
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaListViewModel @Inject constructor(
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val deleteCategoriaUseCase: DeleteCategoriaUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkUserRole()
        getCategorias()
    }

    private fun checkUserRole() {
        viewModelScope.launch {
            authRepository.getSession().collectLatest { user ->
                _uiState.update { it.copy(isAdmin = user?.rol == "Admin") }
            }
        }
    }

    private fun getCategorias() {
        getCategoriasUseCase().onEach { result ->
            when (result) {
                is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                is Resource.Succes -> _uiState.update { it.copy(isLoading = false, categorias = result.data ?: emptyList()) }
                is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CategoriaListUiEvent) {
        when (event) {
            CategoriaListUiEvent.Refresh -> getCategorias()
            is CategoriaListUiEvent.Delete -> {
                viewModelScope.launch {
                    deleteCategoriaUseCase(event.id).collectLatest { result ->
                        if (result is Resource.Succes) {
                            getCategorias()
                        }
                    }
                }
            }
        }
    }
}