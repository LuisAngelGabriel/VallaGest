package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.mappers.toDomain
import edu.ucne.vallagest.data.mappers.toDto
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remotedatasource.CategoriaRemoteDataSource
import edu.ucne.vallagest.domain.categorias.model.Categoria
import edu.ucne.vallagest.domain.categorias.repository.CategoriaRepository
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class CategoriaRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriaRemoteDataSource,
    private val authRepository: AuthRepository
) : CategoriaRepository {

    override fun getCategorias(): Flow<Resource<List<Categoria>>> = flow {
        emit(Resource.Loading())
        remoteDataSource.getCategorias().onSuccess { dtos ->
            emit(Resource.Succes(dtos.map { it.toDomain() }))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error"))
        }
    }

    override fun getCategoria(id: Int): Flow<Resource<Categoria>> = flow {
        emit(Resource.Loading())
        remoteDataSource.getCategoria(id).onSuccess { dto ->
            emit(Resource.Succes(dto.toDomain()))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error"))
        }
    }

    override fun saveCategoria(categoria: Categoria): Flow<Resource<Categoria>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.saveCategoria(categoria.toDto(), user?.rol ?: "Admin").onSuccess { dto ->
            emit(Resource.Succes(dto.toDomain()))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error"))
        }
    }

    override fun updateCategoria(id: Int, categoria: Categoria): Flow<Resource<Categoria>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.updateCategoria(id, categoria.toDto(), user?.rol ?: "Admin").onSuccess {
            emit(Resource.Succes(categoria))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error"))
        }
    }

    override fun deleteCategoria(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.deleteCategoria(id, user?.rol ?: "Admin").onSuccess {
            emit(Resource.Succes(Unit))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error"))
        }
    }
}