package edu.ucne.vallagest.domain.ordenes.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CheckoutDto
import edu.ucne.vallagest.domain.ordenes.model.Orden
import kotlinx.coroutines.flow.Flow

interface OrdenRepository {
    fun realizarPago(usuarioId: Int, meses: Int, dto: CheckoutDto): Flow<Resource<Unit>>
    fun getHistorial(usuarioId: Int): Flow<Resource<List<Orden>>>
    fun cancelarOrden(ordenId: Int): Flow<Resource<Unit>>
}