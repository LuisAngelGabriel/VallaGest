package edu.ucne.vallagest.presentation.vallasocupadas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.vallas.usecases.GetVallasOcupadasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VallaOcupadaViewModel @Inject constructor(
    private val getVallasOcupadasUseCase: GetVallasOcupadasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VallaOcupadaUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getVallasOcupadas()
    }

    fun onEvent(event: VallaOcupadaEvent) {
        when (event) {
            VallaOcupadaEvent.Refresh -> getVallasOcupadas()
        }
    }

    private fun getVallasOcupadas() {
        viewModelScope.launch {
            getVallasOcupadasUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Succes -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            vallas = result.data ?: emptyList(),
                            error = null
                        ) }
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = result.message
                        ) }
                    }
                }
            }
        }
    }
}