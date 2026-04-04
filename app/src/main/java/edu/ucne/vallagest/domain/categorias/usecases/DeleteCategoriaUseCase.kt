package edu.ucne.vallagest.domain.categorias.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.categorias.repository.CategoriaRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class DeleteCategoriaUseCase @Inject constructor(
    private val repository: CategoriaRepository
) {
    operator fun invoke(id: Int): Flow<Resource<Unit>> = repository.deleteCategoria(id)
}