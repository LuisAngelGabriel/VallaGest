package edu.ucne.vallagest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.vallagest.data.local.entities.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Query("SELECT * FROM Categorias ORDER BY categoriaId DESC")
    fun observerAll(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM Categorias WHERE categoriaId = :id")
    suspend fun getById(id: Int): CategoriaEntity?

    @Upsert
    suspend fun upsert(categoria: CategoriaEntity): Long

    @Delete
    suspend fun delete(categoria: CategoriaEntity)

    @Query("DELETE FROM Categorias WHERE categoriaId = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM Categorias WHERE nombre = :nombre")
    suspend fun getCategoriasByNombre(nombre: String): List<CategoriaEntity>
}