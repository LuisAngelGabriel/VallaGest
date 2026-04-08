package edu.ucne.vallagest.domain.ordenes.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.data.remote.dto.CheckoutDto
import edu.ucne.vallagest.domain.ordenes.repository.OrdenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RealizarPagoUseCase @Inject constructor(
    private val repository: OrdenRepository
) {
    operator fun invoke(usuarioId: Int, meses: Int, dto: CheckoutDto): Flow<Resource<Unit>> {
        return repository.realizarPago(usuarioId, meses, dto)
    }

    fun cancelar(ordenId: Int): Flow<Resource<Unit>> {
        return repository.cancelarOrden(ordenId)
    }
}