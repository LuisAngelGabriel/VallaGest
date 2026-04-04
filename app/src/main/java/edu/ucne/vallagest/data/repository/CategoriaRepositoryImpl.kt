package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.CategoriaDao
import edu.ucne.vallagest.data.mappers.toDomain
import edu.ucne.vallagest.data.mappers.toDto
import edu.ucne.vallagest.data.mappers.toEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remotedatasource.CategoriaRemoteDataSource
import edu.ucne.vallagest.domain.categorias.model.Categoria
import edu.ucne.vallagest.domain.categorias.repository.CategoriaRepository
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class CategoriaRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriaRemoteDataSource,
    private val categoriaDao: CategoriaDao,
    private val authRepository: AuthRepository
) : CategoriaRepository {

    override fun getCategorias(): Flow<Resource<List<Categoria>>> = flow {
        emit(Resource.Loading())

        val localCategorias = categoriaDao.observerAll().first()
        if (localCategorias.isNotEmpty()) {
            emit(Resource.Succes(localCategorias.map { it.toDomain() }))
        }

        remoteDataSource.getCategorias().onSuccess { dtos ->
            dtos.forEach { dto ->
                categoriaDao.upsert(dto.toEntity())
            }
            val updatedLocal = categoriaDao.observerAll().first()
            emit(Resource.Succes(updatedLocal.map { it.toDomain() }))
        }.onFailure {
            if (localCategorias.isEmpty()) {
                emit(Resource.Error(it.message ?: "Error de conexión"))
            }
        }
    }

    override fun getCategoria(id: Int): Flow<Resource<Categoria>> = flow {
        emit(Resource.Loading())
        val local = categoriaDao.getById(id)
        local?.let { emit(Resource.Succes(it.toDomain())) }

        remoteDataSource.getCategoria(id).onSuccess { dto ->
            categoriaDao.upsert(dto.toEntity())
            emit(Resource.Succes(dto.toDomain()))
        }
    }

    override fun saveCategoria(categoria: Categoria): Flow<Resource<Categoria>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.saveCategoria(categoria.toDto(), user?.rol ?: "Admin").onSuccess { dto ->
            categoriaDao.upsert(dto.toEntity())
            emit(Resource.Succes(dto.toDomain()))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al guardar"))
        }
    }

    override fun updateCategoria(id: Int, categoria: Categoria): Flow<Resource<Categoria>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.updateCategoria(id, categoria.toDto(), user?.rol ?: "Admin").onSuccess {
            categoriaDao.upsert(categoria.toDto().toEntity())
            emit(Resource.Succes(categoria))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al actualizar"))
        }
    }

    override fun deleteCategoria(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.deleteCategoria(id, user?.rol ?: "Admin").onSuccess {
            categoriaDao.deleteById(id)
            emit(Resource.Succes(Unit))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al eliminar"))
        }
    }
}