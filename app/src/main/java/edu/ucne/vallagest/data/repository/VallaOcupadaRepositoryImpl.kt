package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.VallaOcupadaDao
import edu.ucne.vallagest.data.mappers.toDomain
import edu.ucne.vallagest.data.mappers.toEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.remotedatasource.VallaOcupadaRemoteDataSource
import edu.ucne.vallagest.domain.vallas.model.VallaOcupada
import edu.ucne.vallagest.domain.vallas.repository.VallaOcupadaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*

class VallaOcupadaRepositoryImpl @Inject constructor(
    private val remoteDataSource: VallaOcupadaRemoteDataSource,
    private val vallaOcupadaDao: VallaOcupadaDao
) : VallaOcupadaRepository {

    override fun getVallasOcupadas(): Flow<Resource<List<VallaOcupada>>> = flow {
        emit(Resource.Loading())

        val localData = vallaOcupadaDao.observerAll().first()
        emit(Resource.Succes(localData.map { it.toDomain() }))

        remoteDataSource.getVallasOcupadas().onSuccess { dtos ->
            vallaOcupadaDao.clearAll()
            vallaOcupadaDao.upsertAll(dtos.map { it.toEntity() })

            vallaOcupadaDao.observerAll().collect { freshLocal ->
                emit(Resource.Succes(freshLocal.map { it.toDomain() }))
            }
        }.onFailure { error ->
            if (localData.isEmpty()) {
                emit(Resource.Error(error.message ?: "Error al conectar con el servidor"))
            }
        }
    }
}