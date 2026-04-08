package edu.ucne.vallagest.domain.usuarios.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.AuthResponse
import edu.ucne.vallagest.data.remote.dto.RegisterRequest
import edu.ucne.vallagest.domain.usuarios.model.Usuario
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Resource<Usuario>>
    fun registro(nombre: String, email: String, password: String): Flow<Resource<Usuario>>
    fun getSession(): Flow<Usuario?>
    suspend fun actualizarUsuario(id: Int, request: RegisterRequest): Flow<Resource<AuthResponse>>
}