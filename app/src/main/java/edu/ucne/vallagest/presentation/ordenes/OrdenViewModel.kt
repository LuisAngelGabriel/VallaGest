package edu.ucne.vallagest.presentation.ordenes

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CheckoutDto
import edu.ucne.vallagest.domain.ordenes.usecases.GetHistorialUseCase
import edu.ucne.vallagest.domain.ordenes.usecases.RealizarPagoUseCase
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import edu.ucne.vallagest.domain.vallas.repository.UploadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class OrdenViewModel @Inject constructor(
    private val realizarPagoUseCase: RealizarPagoUseCase,
    private val getHistorialUseCase: GetHistorialUseCase,
    private val authRepository: AuthRepository,
    private val uploadRepository: UploadRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrdenUiState())
    val state = _state.asStateFlow()

    init {
        cargarHistorial()
    }

    fun onEvent(event: OrdenUiEvent) {
        when (event) {
            is OrdenUiEvent.OnMesesChange -> _state.update { it.copy(meses = event.meses) }
            is OrdenUiEvent.OnMetodoChange -> _state.update { it.copy(metodoPago = event.metodo) }
            is OrdenUiEvent.Pagar -> realizarCobro()
            is OrdenUiEvent.GetHistorial -> cargarHistorial()
            is OrdenUiEvent.ClearError -> _state.update { it.copy(error = null) }
        }
    }

    fun uploadComprobante(uri: Uri, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val file = uriToFile(uri, context) ?: return@launch
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            uploadRepository.uploadImage(body).collect { result ->
                _state.update {
                    when (result) {
                        is Resource.Loading -> it.copy(isLoading = true)
                        is Resource.Succes -> it.copy(isLoading = false, comprobanteUrl = result.data?.url)
                        is Resource.Error -> it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    private suspend fun uriToFile(uri: Uri, context: Context): File? = withContext(Dispatchers.IO) {
        val file = File(context.cacheDir, "comprobante_${System.currentTimeMillis()}.jpg")
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output -> input.copyTo(output) }
            }
            file
        } catch (e: Exception) {
            null
        }
    }

    private fun realizarCobro() {
        viewModelScope.launch {
            val usuario = authRepository.getSession().firstOrNull()
            if (usuario == null || usuario.usuarioId == 0) return@launch

            val currentState = _state.value
            val dto = CheckoutDto(
                metodo = currentState.metodoPago,
                comprobanteUrl = currentState.comprobanteUrl ?: ""
            )

            realizarPagoUseCase(usuario.usuarioId, currentState.meses, dto).collect { result ->
                _state.update {
                    when (result) {
                        is Resource.Loading -> it.copy(isLoading = true)
                        is Resource.Succes -> it.copy(isLoading = false, pagoExitoso = true)
                        is Resource.Error -> it.copy(isLoading = false, error = result.message)
                    }
                }
                if (result is Resource.Succes) {
                    cargarHistorial()
                }
            }
        }
    }

    fun cargarHistorial() {
        viewModelScope.launch {
            val usuario = authRepository.getSession().firstOrNull()
            if (usuario == null || usuario.usuarioId == 0) return@launch

            getHistorialUseCase(usuario.usuarioId).collect { result ->
                _state.update {
                    when (result) {
                        is Resource.Loading -> it.copy(isLoading = true)
                        is Resource.Succes -> it.copy(isLoading = false, ordenes = result.data ?: emptyList())
                        is Resource.Error -> it.copy(isLoading = false, error = result.message)
                    }
                }
            }
        }
    }

    fun cancelarAlquiler(ordenId: Int) {
        viewModelScope.launch {
            realizarPagoUseCase.cancelar(ordenId).collect { result ->
                if (result is Resource.Succes) {
                    cargarHistorial()
                }
            }
        }
    }
}