package edu.ucne.vallagest.presentation.vallas.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.categorias.repository.CategoriaRepository
import edu.ucne.vallagest.domain.vallas.model.Valla
import edu.ucne.vallagest.domain.vallas.repository.UploadRepository
import edu.ucne.vallagest.domain.vallas.usecases.GetVallaUseCase
import edu.ucne.vallagest.domain.vallas.usecases.SaveVallaUseCase
import edu.ucne.vallagest.domain.vallas.usecases.UpdateVallaUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class EditVallaViewModel @Inject constructor(
    private val getVallaUseCase: GetVallaUseCase,
    private val saveVallaUseCase: SaveVallaUseCase,
    private val updateVallaUseCase: UpdateVallaUseCase,
    private val categoriaRepository: CategoriaRepository,
    private val uploadRepository: UploadRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditVallaUiState())
    val state = _state.asStateFlow()

    init {
        loadCategorias()
    }

    private fun loadCategorias() {
        viewModelScope.launch {
            categoriaRepository.getCategorias().collectLatest { result ->
                if (result is Resource.Succes) {
                    _state.update { it.copy(categorias = result.data ?: emptyList()) }
                }
            }
        }
    }

    fun onEvent(event: EditVallaUiEvent, context: Context? = null) {
        when (event) {
            is EditVallaUiEvent.Load -> onLoad(event.id)
            is EditVallaUiEvent.NombreChanged -> _state.update { it.copy(nombre = event.value, nombreError = null) }
            is EditVallaUiEvent.UbicacionChanged -> _state.update { it.copy(ubicacion = event.value, ubicacionError = null) }
            is EditVallaUiEvent.PrecioChanged -> _state.update { it.copy(precioMensual = event.value, precioError = null) }
            is EditVallaUiEvent.DescripcionChanged -> _state.update { it.copy(descripcion = event.value) }
            is EditVallaUiEvent.CategoriaChanged -> _state.update { it.copy(categoriaId = event.id) }
            is EditVallaUiEvent.ImagenSeleccionada -> {
                context?.let { uploadImage(Uri.parse(event.uri), it) }
            }
            EditVallaUiEvent.Save -> onSave()
        }
    }

    private fun uploadImage(uri: Uri, context: Context) {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
                val outputStream = FileOutputStream(file)
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                uploadRepository.uploadImage(body).collectLatest { result ->
                    when (result) {
                        is Resource.Succes -> {
                            _state.update { it.copy(
                                imagenUrl = result.data?.url ?: "",
                                isSaving = false
                            )}
                        }
                        is Resource.Error -> {
                            _state.update { it.copy(isSaving = false, error = result.message) }
                        }
                        is Resource.Loading -> {
                            _state.update { it.copy(isSaving = true) }
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    private fun onLoad(id: Int) {
        if (id == 0) return
        viewModelScope.launch {
            getVallaUseCase(id).collectLatest { result ->
                if (result is Resource.Succes) {
                    result.data?.let { valla ->
                        _state.update { it.copy(
                            isNew = false,
                            vallaId = valla.vallaId,
                            nombre = valla.nombre,
                            ubicacion = valla.ubicacion,
                            precioMensual = valla.precioMensual.toString(),
                            descripcion = valla.descripcion ?: "",
                            imagenUrl = valla.imagenUrl,
                            categoriaId = valla.categoriaId
                        )}
                    }
                }
            }
        }
    }

    private fun onSave() {
        if (!validate()) return
        val currentState = _state.value

        viewModelScope.launch {
            val valla = Valla(
                vallaId = if (currentState.isNew) 0 else (currentState.vallaId ?: 0),
                nombre = currentState.nombre,
                ubicacion = currentState.ubicacion,
                precioMensual = currentState.precioMensual.toDoubleOrNull() ?: 0.0,
                descripcion = currentState.descripcion,
                estaOcupada = false,
                categoriaId = currentState.categoriaId,
                imagenUrl = currentState.imagenUrl
            )

            val flow = if (currentState.isNew) saveVallaUseCase(valla) else updateVallaUseCase(valla.vallaId, valla)

            flow.collectLatest { result ->
                when (result) {
                    is Resource.Succes -> _state.update { it.copy(saved = true, isSaving = false) }
                    is Resource.Error -> _state.update { it.copy(isSaving = false, error = result.message) }
                    is Resource.Loading -> _state.update { it.copy(isSaving = true) }
                }
            }
        }
    }

    private fun validate(): Boolean {
        var valid = true
        if (_state.value.nombre.isBlank()) {
            _state.update { it.copy(nombreError = "Requerido") }
            valid = false
        }
        if (_state.value.ubicacion.isBlank()) {
            _state.update { it.copy(ubicacionError = "Requerido") }
            valid = false
        }
        if (_state.value.precioMensual.toDoubleOrNull() == null) {
            _state.update { it.copy(precioError = "Inválido") }
            valid = false
        }
        return valid
    }
}