package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.CarritoDao
import edu.ucne.vallagest.data.mappers.toDomain
import edu.ucne.vallagest.data.mappers.toEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CarritoPostDto
import edu.ucne.vallagest.data.remote.remotedatasource.CarritoRemoteDataSource
import edu.ucne.vallagest.domain.carrito.model.CarritoItem
import edu.ucne.vallagest.domain.carrito.repository.CarritoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class CarritoRepositoryImpl @Inject constructor(
    private val remoteDataSource: CarritoRemoteDataSource,
    private val carritoDao: CarritoDao
) : CarritoRepository {

    override fun getCarrito(usuarioId: Int): Flow<Resource<List<CarritoItem>>> = flow {
        emit(Resource.Loading())

        val localItems = carritoDao.getCarritoLocal().first()
        if (localItems.isNotEmpty()) {
            emit(Resource.Succes(localItems.map { it.toDomain() }))
        }

        remoteDataSource.getCarrito(usuarioId).onSuccess { dtos ->
            carritoDao.clearCarrito()
            dtos.forEach { dto ->
                carritoDao.insertCarrito(listOf(dto.toEntity()))
            }

            val updatedLocal = carritoDao.getCarritoLocal().first()
            emit(Resource.Succes(updatedLocal.map { it.toDomain() }))

        }.onFailure {
            if (localItems.isEmpty()) {
                emit(Resource.Error(it.message ?: "Error de conexión"))
            }
        }
    }

    override fun agregarAlCarrito(item: CarritoPostDto): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        remoteDataSource.agregarAlCarrito(item).onSuccess {
            emit(Resource.Succes(Unit))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al agregar"))
        }
    }

    override fun eliminarDelCarrito(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        remoteDataSource.eliminarDelCarrito(id).onSuccess {
            carritoDao.deleteById(id)
            emit(Resource.Succes(Unit))
        }.onFailure {
            emit(Resource.Error(it.message ?: "Error al eliminar"))
        }
    }
}