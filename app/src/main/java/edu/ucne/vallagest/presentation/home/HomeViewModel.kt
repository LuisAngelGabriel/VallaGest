package edu.ucne.vallagest.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
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
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state = _state.asStateFlow()

    val usuarioLogueado: StateFlow<Usuario?> = authRepository.getSession()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        observeVallas()
    }

    private fun observeVallas() {
        viewModelScope.launch {
            getVallasUseCase().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> _state.update { it.copy(isLoading = true) }
                    is Resource.Succes -> _state.update { it.copy(isLoading = false, vallas = result.data ?: emptyList()) }
                    is Resource.Error -> _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteVallaUseCase(id).collectLatest { result ->
                if (result is Resource.Succes) {
                    observeVallas()
                }
            }
        }
    }
}