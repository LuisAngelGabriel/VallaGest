package edu.ucne.vallagest.domain.carrito.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CarritoPostDto
import edu.ucne.vallagest.domain.carrito.repository.CarritoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class AddCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    operator fun invoke(item: CarritoPostDto): Flow<Resource<Unit>> {
        return repository.agregarAlCarrito(item)
    }
}