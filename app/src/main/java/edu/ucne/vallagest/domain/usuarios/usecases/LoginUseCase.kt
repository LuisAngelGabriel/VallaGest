package edu.ucne.vallagest.domain.usuarios.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.usuarios.model.Usuario
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Resource<Usuario>> {
        return repository.login(email, password)
    }
}