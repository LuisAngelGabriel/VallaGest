package edu.ucne.vallagest.data.repository

import edu.ucne.vallagest.data.local.dao.CarritoDao
import edu.ucne.vallagest.data.local.dao.OrdenDao
import edu.ucne.vallagest.data.mappers.toDomain
import edu.ucne.vallagest.data.mappers.toEntity
import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CheckoutDto
import edu.ucne.vallagest.data.remote.remotedatasource.OrdenRemoteDataSource
import edu.ucne.vallagest.domain.ordenes.model.Orden
import edu.ucne.vallagest.domain.ordenes.repository.OrdenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrdenRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrdenRemoteDataSource,
    private val ordenDao: OrdenDao,
    private val carritoDao: CarritoDao
) : OrdenRepository {

    override fun realizarPago(usuarioId: Int, meses: Int, dto: CheckoutDto): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.realizarCheckout(usuarioId, meses, dto)
        if (result.isSuccess) {
            carritoDao.clearCarrito()
            emit(Resource.Succes(Unit))
        } else {
            emit(Resource.Error(result.exceptionOrNull()?.message ?: "Error al procesar el pago"))
        }
    }

    override fun getHistorial(usuarioId: Int): Flow<Resource<List<Orden>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getHistorial(usuarioId)
            if (result.isSuccess) {
                val dtos = result.getOrNull() ?: emptyList()
                ordenDao.clearDetallesByUsuario(usuarioId)
                ordenDao.clearOrdenes(usuarioId)
                dtos.forEach { dto ->
                    ordenDao.insertOrden(dto.toEntity(usuarioId))
                    ordenDao.insertDetalles(dto.detalles.map { it.toEntity(dto.ordenId) })
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error de conexión"))
        }

        val entidades = ordenDao.getOrdenes(usuarioId).first()
        val ordenesCompletas = entidades.map { ordenEntity ->
            val detallesEntity = ordenDao.getDetallesByOrden(ordenEntity.ordenId)
            ordenEntity.toDomain(detallesEntity)
        }
        emit(Resource.Succes(ordenesCompletas))
    }

    override fun cancelarOrden(ordenId: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val result = remoteDataSource.cancelarOrden(ordenId)
        if (result.isSuccess) {
            emit(Resource.Succes(Unit))
        } else {
            emit(Resource.Error(result.exceptionOrNull()?.message ?: "Error al cancelar orden"))
        }
    }
}