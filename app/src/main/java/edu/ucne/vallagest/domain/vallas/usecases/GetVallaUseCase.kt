package edu.ucne.vallagest.domain.vallas.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.vallas.model.Valla
import edu.ucne.vallagest.domain.vallas.repository.VallaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetVallaUseCase @Inject constructor(
    private val repository: VallaRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Valla>> = repository.getValla(id)
}