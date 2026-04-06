package edu.ucne.vallagest.domain.categorias.usecases

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.categorias.model.Categoria
import edu.ucne.vallagest.domain.categorias.repository.CategoriaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCategoriasUseCase @Inject constructor(
    private val repository: CategoriaRepository
) {
    operator fun invoke(): Flow<Resource<List<Categoria>>> = repository.getCategorias()
}