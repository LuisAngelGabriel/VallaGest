package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.UsuarioDao
import edu.ucne.vallagest.data.mappers.toEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.LoginRequest
import edu.ucne.vallagest.data.remote.dto.RegisterRequest
import edu.ucne.vallagest.data.remote.remotedatasource.AuthRemoteDataSource
import edu.ucne.vallagest.domain.usuarios.model.Usuario
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val usuarioDao: UsuarioDao
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Resource<Usuario>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.login(LoginRequest(email, password))

        result.onSuccess { dto ->
            usuarioDao.clearAll()
            usuarioDao.saveUsuario(dto.toEntity())
            emit(Resource.Succes(Usuario(dto.usuarioId, dto.nombre, dto.email, dto.rol)))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Credenciales incorrectas"))
        }
    }

    override fun registro(nombre: String, email: String, password: String): Flow<Resource<Usuario>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.registro(RegisterRequest(nombre, email, password))

        result.onSuccess { dto ->
            usuarioDao.clearAll()
            usuarioDao.saveUsuario(dto.toEntity())
            emit(Resource.Succes(Usuario(dto.usuarioId, dto.nombre, dto.email, dto.rol)))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error en el registro"))
        }
    }

    override fun getSession(): Flow<Usuario?> {
        return usuarioDao.getLoggedUsuario().map { entity ->
            entity?.let { Usuario(it.usuarioId, it.nombre, it.email, it.rol) }
        }
    }
}