package edu.ucne.vallagest.domain.categorias.repository

import edu.ucne.vallagest.data.remote.Resource
import edu.ucne.vallagest.domain.categorias.model.Categoria
import kotlinx.coroutines.flow.Flow

interface CategoriaRepository {
    fun getCategorias(): Flow<Resource<List<Categoria>>>
    fun getCategoria(id: Int): Flow<Resource<Categoria>>
    fun saveCategoria(categoria: Categoria): Flow<Resource<Categoria>>
    fun updateCategoria(id: Int, categoria: Categoria): Flow<Resource<Categoria>>
    fun deleteCategoria(id: Int): Flow<Resource<Unit>>
}