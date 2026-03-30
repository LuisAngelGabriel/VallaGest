package edu.ucne.vallagest.domain.usuarios.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.usuarios.model.Usuario
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class RegistroUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(nombre: String, email: String, password: String): Flow<Resource<Usuario>> {
        return repository.registro(nombre, email, password)
    }
}