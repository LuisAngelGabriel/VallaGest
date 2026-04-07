package edu.ucne.vallagest.domain.carrito.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.carrito.repository.CarritoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class DeleteCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Unit>> {
        return repository.eliminarDelCarrito(id)
    }
}