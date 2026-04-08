package edu.ucne.vallagest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.vallagest.data.local.entities.VallaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VallaDao {
    @Query("SELECT * FROM Vallas ORDER BY vallaId DESC")
    fun observerAll(): Flow<List<VallaEntity>>

    @Query("SELECT * FROM Vallas WHERE vallaId = :id")
    suspend fun getById(id: Int): VallaEntity?

    @Upsert
    suspend fun upsert(valla: VallaEntity): Long

    @Upsert
    suspend fun upsertAll(vallas: List<VallaEntity>)

    @Delete
    suspend fun delete(valla: VallaEntity)

    @Query("DELETE FROM Vallas WHERE vallaId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM Vallas")
    suspend fun clearAll()

    @Query("SELECT * FROM Vallas WHERE nombre = :nombre")
    suspend fun getVallasByNombre(nombre: String): List<VallaEntity>
}