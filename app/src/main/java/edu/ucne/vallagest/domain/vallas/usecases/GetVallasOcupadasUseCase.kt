package edu.ucne.vallagest.domain.vallas.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.vallas.model.VallaOcupada
import edu.ucne.vallagest.domain.vallas.repository.VallaOcupadaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetVallasOcupadasUseCase @Inject constructor(
    private val repository: VallaOcupadaRepository
) {
    operator fun invoke(): Flow<Resource<List<VallaOcupada>>> {
        return repository.getVallasOcupadas()
    }
}