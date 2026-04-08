package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.CategoriaDao
import edu.ucne.vallagest.data.mappers.*
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remotedatasource.CategoriaRemoteDataSource
import edu.ucne.vallagest.domain.categorias.model.Categoria
import edu.ucne.vallagest.domain.categorias.repository.CategoriaRepository
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*

class CategoriaRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriaRemoteDataSource,
    private val categoriaDao: CategoriaDao,
    private val authRepository: AuthRepository
) : CategoriaRepository {

    override fun getCategorias(): Flow<Resource<List<Categoria>>> = flow {
        emit(Resource.Loading())
        val local = categoriaDao.observerAll().first()
        emit(Resource.Succes(local.map { it.toDomain() }))

        val user = authRepository.getSession().firstOrNull()
        val pendientes = categoriaDao.observerAll().first().filter { !it.isSynced }

        pendientes.forEach { entity ->
            try {
                if (entity.categoriaId < 0) {
                    remoteDataSource.saveCategoria(entity.toDomain().toDto().copy(categoriaId = 0), user?.rol ?: "Admin").onSuccess { dto ->
                        categoriaDao.deleteById(entity.categoriaId)
                        categoriaDao.upsert(dto.toEntity().copy(isSynced = true))
                    }
                } else {
                    remoteDataSource.updateCategoria(entity.categoriaId, entity.toDomain().toDto(), user?.rol ?: "Admin").onSuccess {
                        categoriaDao.upsert(entity.copy(isSynced = true))
                    }
                }
            } catch (e: Exception) { }
        }

        remoteDataSource.getCategorias().onSuccess { dtos ->
            val noSincronizados = categoriaDao.observerAll().first().filter { !it.isSynced }
            categoriaDao.clearAll()
            categoriaDao.upsertAll(dtos.map { it.toEntity() })
            if (noSincronizados.isNotEmpty()) categoriaDao.upsertAll(noSincronizados)

            categoriaDao.observerAll().collect { updatedLocal ->
                emit(Resource.Succes(updatedLocal.map { it.toDomain() }))
            }
        }.onFailure {
            if (local.isEmpty()) emit(Resource.Error(it.message ?: "Error de conexión"))
        }
    }

    override fun saveCategoria(categoria: Categoria): Flow<Resource<Categoria>> = flow {
        val tempId = if (categoria.categoriaId <= 0) (System.currentTimeMillis() % 1000000).toInt() * -1 else categoria.categoriaId
        val localEntity = categoria.toDto().toEntity().copy(categoriaId = tempId, isSynced = false)
        categoriaDao.upsert(localEntity)
        emit(Resource.Succes(localEntity.toDomain()))

        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.saveCategoria(categoria.toDto().copy(categoriaId = 0), user?.rol ?: "Admin").onSuccess { dto ->
            categoriaDao.deleteById(tempId)
            categoriaDao.upsert(dto.toEntity().copy(isSynced = true))
        }
    }

    override fun updateCategoria(id: Int, categoria: Categoria): Flow<Resource<Categoria>> = flow {
        val localEntity = categoria.toDto().toEntity().copy(categoriaId = id, isSynced = false)
        categoriaDao.upsert(localEntity)
        emit(Resource.Succes(categoria))

        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.updateCategoria(id, categoria.toDto(), user?.rol ?: "Admin").onSuccess {
            categoriaDao.upsert(localEntity.copy(isSynced = true))
        }
    }

    override fun getCategoria(id: Int): Flow<Resource<Categoria>> = flow {
        emit(Resource.Loading())
        categoriaDao.getById(id)?.let { emit(Resource.Succes(it.toDomain())) }
        remoteDataSource.getCategoria(id).onSuccess { dto ->
            categoriaDao.upsert(dto.toEntity())
            emit(Resource.Succes(dto.toDomain()))
        }
    }

    override fun deleteCategoria(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        categoriaDao.deleteById(id)
        emit(Resource.Succes(Unit))
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.deleteCategoria(id, user?.rol ?: "Admin")
    }
}