package edu.ucne.vallagest.domain.usuarios.usecases

import edu.ucne.vallagest.domain.usuarios.model.Usuario
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Usuario?> {
        return repository.getSession()
    }
}