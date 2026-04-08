package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.CarritoDao
import edu.ucne.vallagest.data.local.dao.VallaDao
import edu.ucne.vallagest.data.local.entities.CarritoEntity
import edu.ucne.vallagest.data.mappers.toDomain
import edu.ucne.vallagest.data.mappers.toEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CarritoPostDto
import edu.ucne.vallagest.data.remote.remotedatasource.CarritoRemoteDataSource
import edu.ucne.vallagest.domain.carrito.model.CarritoItem
import edu.ucne.vallagest.domain.carrito.repository.CarritoRepository
import edu.ucne.vallagest.domain.usuarios.repository.AuthRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.*

class CarritoRepositoryImpl @Inject constructor(
    private val remoteDataSource: CarritoRemoteDataSource,
    private val carritoDao: CarritoDao,
    private val vallaDao: VallaDao,
    private val authRepository: AuthRepository
) : CarritoRepository {

    override fun getCarrito(usuarioId: Int): Flow<Resource<List<CarritoItem>>> = flow {
        emit(Resource.Loading())

        val local = carritoDao.getCarritoLocal().first()
        emit(Resource.Succes(local.map { it.toDomain() }))

        val pendientes = local.filter { !it.isSynced }
        if (pendientes.isNotEmpty()) {
            pendientes.forEach { entity ->
                remoteDataSource.agregarAlCarrito(CarritoPostDto(usuarioId, entity.vallaId)).onSuccess {
                    carritoDao.deleteById(entity.carritoItemId)
                }
            }
        }

        remoteDataSource.getCarrito(usuarioId).onSuccess { dtos ->
            val localActual = carritoDao.getCarritoLocal().first()
            val noSincronizados = localActual.filter { !it.isSynced }

            carritoDao.clearCarrito()
            carritoDao.insertCarrito(dtos.map { it.toEntity() })
            if (noSincronizados.isNotEmpty()) carritoDao.insertCarrito(noSincronizados)

            carritoDao.getCarritoLocal().collect { freshLocal ->
                emit(Resource.Succes(freshLocal.map { it.toDomain() }))
            }
        }.onFailure {
            if (local.isEmpty()) emit(Resource.Error(it.message ?: "Error de conexión"))
        }
    }

    override fun agregarAlCarrito(item: CarritoPostDto): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())

        val vallaLocal = vallaDao.getById(item.vallaId)
        val tempId = (System.currentTimeMillis() % 1000000).toInt() * -1

        if (vallaLocal != null) {
            val localEntity = CarritoEntity(
                carritoItemId = tempId,
                vallaId = vallaLocal.vallaId,
                nombreValla = vallaLocal.nombre,
                precio = vallaLocal.precioMensual,
                imagenUrl = vallaLocal.imagenUrl,
                isSynced = false
            )
            carritoDao.insertCarrito(listOf(localEntity))
            emit(Resource.Succes(Unit))
        }

        remoteDataSource.agregarAlCarrito(item).onSuccess {
            remoteDataSource.getCarrito(item.usuarioId).onSuccess { dtos ->
                carritoDao.deleteById(tempId)
                carritoDao.insertCarrito(dtos.map { it.toEntity() })
            }
        }.onFailure {
            if (vallaLocal == null) emit(Resource.Error("No se pudo agregar al carrito"))
        }
    }

    override fun eliminarDelCarrito(id: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        carritoDao.deleteById(id)
        emit(Resource.Succes(Unit))
        remoteDataSource.eliminarDelCarrito(id)
    }
}