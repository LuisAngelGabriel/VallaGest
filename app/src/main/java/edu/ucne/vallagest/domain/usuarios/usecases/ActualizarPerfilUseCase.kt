package edu.ucne.vallagest.domain.usuarios.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.AuthResponse
import edu.ucne.vallagest.data.remote.dto.RegisterRequest
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ActualizarPerfilUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(id: Int, request: RegisterRequest): Flow<Resource<AuthResponse>> {
        return repository.actualizarUsuario(id, request)
    }
}