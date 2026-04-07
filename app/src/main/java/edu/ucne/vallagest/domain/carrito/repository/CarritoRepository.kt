package edu.ucne.vallagest.domain.carrito.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CarritoPostDto
import edu.ucne.vallagest.domain.carrito.model.CarritoItem
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {
    fun getCarrito(usuarioId: Int): Flow<Resource<List<CarritoItem>>>
    fun agregarAlCarrito(item: CarritoPostDto): Flow<Resource<Unit>>
    fun eliminarDelCarrito(id: Int): Flow<Resource<Unit>>
}