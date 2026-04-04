package edu.ucne.vallagest.presentation.categorias.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.categorias.model.Categoria
import edu.ucne.vallagest.domain.categorias.usecases.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaEditViewModel @Inject constructor(
    private val saveCategoriaUseCase: SaveCategoriaUseCase,
    private val updateCategoriaUseCase: UpdateCategoriaUseCase,
    private val getCategoriaUseCase: GetCategoriaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaEditUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: CategoriaEditUiEvent) {
        when (event) {
            is CategoriaEditUiEvent.NombreChanged -> _uiState.update { it.copy(nombre = event.nombre, nombreError = null) }
            is CategoriaEditUiEvent.DescripcionChanged -> _uiState.update { it.copy(descripcion = event.descripcion) }
            CategoriaEditUiEvent.Save -> save()
        }
    }

    fun loadCategoria(id: Int) {
        if (id <= 0) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, categoriaId = id) }

            getCategoriaUseCase(id).collectLatest { result ->
                when (result) {
                    is Resource.Succes -> {
                        result.data?.let { cat ->
                            _uiState.update { it.copy(
                                isLoading = false,
                                categoriaId = cat.categoriaId,
                                nombre = cat.nombre,
                                descripcion = cat.descripcion
                            )}
                        }
                    }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun save() {
        val state = _uiState.value
        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(nombreError = "El nombre es requerido") }
            return
        }

        viewModelScope.launch {
            val categoria = Categoria(
                categoriaId = state.categoriaId,
                nombre = state.nombre,
                descripcion = state.descripcion
            )

            val flow = if (categoria.categoriaId == 0) {
                saveCategoriaUseCase(categoria)
            } else {
                updateCategoriaUseCase(categoria.categoriaId, categoria)
            }

            flow.collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Succes -> _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                    is Resource.Error -> _uiState.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }
}