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
        if (local.isNotEmpty()) emit(Resource.Succes(local.map { it.toDomain() }))

        remoteDataSource.getVallas().onSuccess { dtos ->
            vallaDao.clearAll()
            dtos.forEach { vallaDao.upsert(it.toEntity()) }
            vallaDao.observerAll().collect { freshLocal ->
                emit(Resource.Succes(freshLocal.map { it.toDomain() }))
            }
        }.onFailure {
            if (local.isEmpty()) emit(Resource.Error(it.message ?: "Sin conexión"))
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

    override fun saveValla(valla: Valla): Flow<Resource<Valla>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.saveValla(valla.toDto(), user?.rol ?: "Admin").onSuccess { dto ->
            vallaDao.upsert(dto.toEntity())
            emit(Resource.Succes(dto.toDomain()))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al guardar"))
        }
    }

    override fun updateValla(id: Int, valla: Valla): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.updateValla(id, valla.toDto(), user?.rol ?: "Admin").onSuccess {
            vallaDao.upsert(valla.toDto().toEntity())
            emit(Resource.Succes(Unit))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al actualizar"))
        }
    }

    override fun deleteValla(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val user = authRepository.getSession().firstOrNull()
        remoteDataSource.deleteValla(id, user?.rol ?: "Admin").onSuccess {
            vallaDao.deleteById(id)
            emit(Resource.Succes(Unit))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al eliminar"))
        }
    }

    override fun uploadImage(file: MultipartBody.Part): Flow<Resource<UploadResponseDto>> = flow {
        emit(Resource.Loading())
        remoteDataSource.uploadImage(file).onSuccess { dto ->
            emit(Resource.Succes(dto))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al subir imagen"))
        }
    }
}