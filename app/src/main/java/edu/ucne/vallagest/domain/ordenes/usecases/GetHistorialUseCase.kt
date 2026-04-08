package edu.ucne.vallagest.domain.ordenes.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.ordenes.model.Orden
import edu.ucne.vallagest.domain.ordenes.repository.OrdenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistorialUseCase @Inject constructor(
    private val repository: OrdenRepository
) {
    operator fun invoke(usuarioId: Int): Flow<Resource<List<Orden>>> {
        return repository.getHistorial(usuarioId)
    }
}