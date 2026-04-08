package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.VallaDao
import edu.ucne.vallagest.data.mappers.*
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.UploadResponseDto
import edu.ucne.vallagest.data.remotedatasource.VallaRemoteDataSource
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import edu.ucne.vallagest.domain.vallas.model.Valla
import edu.ucne.vallagest.domain.vallas.repository.UploadRepository
import edu.ucne.vallagest.domain.vallas.repository.VallaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody

class VallaRepositoryImpl @Inject constructor(
    private val remoteDataSource: VallaRemoteDataSource,
    private val vallaDao: VallaDao,
    private val authRepository: AuthRepository
) : VallaRepository, UploadRepository {

    override fun getVallas(): Flow<Resource<List<Valla>>> = flow {
        emit(Resource.Loading())
        val local = vallaDao.observerAll().first()
        emit(Resource.Succes(local.map { it.toDomain() }))

        val user = authRepository.getSession().firstOrNull()
        val pendientes = vallaDao.observerAll().first().filter { !it.isSynced }

        pendientes.forEach { entity ->
            try {
                if (entity.vallaId < 0) {
                    remoteDataSource.saveValla(entity.toDomain().toDto().copy(vallaId = 0), user?.rol ?: "Admin").onSuccess { dto ->
                        vallaDao.deleteById(entity.vallaId)
                        vallaDao.upsert(dto.toEntity().copy(isSynced = true))
                    }
                } else {
                    remoteDataSource.updateValla(entity.vallaId, entity.toDomain().toDto(), user?.rol ?: "Admin").onSuccess {
                        vallaDao.upsert(entity.copy(isSynced = true))
                    }
                }
            } catch (e: Exception) { }
        }

        remoteDataSource.getVallas().onSuccess { dtos ->
            val noSincronizados = vallaDao.observerAll().first().filter { !it.isSynced }
            vallaDao.clearAll()
            vallaDao.upsertAll(dtos.map { it.toEntity() })
            if (noSincronizados.isNotEmpty()) vallaDao.upsertAll(noSincronizados)

            vallaDao.observerAll().collect { freshLocal ->
                emit(Resource.Succes(freshLocal.map { it.toDomain() }))
            }
        }.onFailure {
            if (local.isEmpty()) emit(Resource.Error(it.message ?: "Sin conexión"))
        }
    }

    override fun saveValla(valla: Valla): Flow<Resource<Valla>> = flow {
        val tempId = if (valla.vallaId <= 0) (System.currentTimeMillis() % 1000000).toInt() * -1 else valla.vallaId
        val localEntity = valla.toDto().toEntity().copy(vallaId = tempId, isSynced = false)
        vallaDao.upsert(localEntity)
        emit(Resource.Succes(localEntity.toDomain()))

        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.saveValla(valla.toDto().copy(vallaId = 0), user?.rol ?: "Admin").onSuccess { dto ->
            vallaDao.deleteById(tempId)
            vallaDao.upsert(dto.toEntity().copy(isSynced = true))
        }
    }

    override fun updateValla(id: Int, valla: Valla): Flow<Resource<Unit>> = flow {
        val localEntity = valla.toDto().toEntity().copy(vallaId = id, isSynced = false)
        vallaDao.upsert(localEntity)
        emit(Resource.Succes(Unit))

        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.updateValla(id, valla.toDto(), user?.rol ?: "Admin").onSuccess {
            vallaDao.upsert(localEntity.copy(isSynced = true))
        }
    }

    override fun getValla(id: Int): Flow<Resource<Valla>> = flow {
        emit(Resource.Loading())
        vallaDao.getById(id)?.let { emit(Resource.Succes(it.toDomain())) }
        remoteDataSource.getValla(id).onSuccess { dto ->
            vallaDao.upsert(dto.toEntity())
            emit(Resource.Succes(dto.toDomain()))
        }
    }

    override fun deleteValla(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        vallaDao.deleteById(id)
        emit(Resource.Succes(Unit))
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.deleteValla(id, user?.rol ?: "Admin")
    }

    override fun uploadImage(file: MultipartBody.Part): Flow<Resource<UploadResponseDto>> = flow {
        emit(Resource.Loading())
        remoteDataSource.uploadImage(file).onSuccess { dto ->
            emit(Resource.Succes(dto))
        }
    }
}