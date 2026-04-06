package edu.ucne.vallagest.domain.vallas.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.vallas.model.Valla
import edu.ucne.vallagest.domain.vallas.repository.VallaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class SaveVallaUseCase @Inject constructor(
    private val repository: VallaRepository
) {
    operator fun invoke(valla: Valla): Flow<Resource<Valla>> = repository.saveValla(valla)
}