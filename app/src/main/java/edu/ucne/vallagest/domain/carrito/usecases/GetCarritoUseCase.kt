package edu.ucne.vallagest.domain.carrito.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.carrito.model.CarritoItem
import edu.ucne.vallagest.domain.carrito.repository.CarritoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    operator fun invoke(usuarioId: Int): Flow<Resource<List<CarritoItem>>> {
        return repository.getCarrito(usuarioId)
    }
}