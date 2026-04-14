package edu.ucne.vallagest.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.vallagest.data.local.entities.VallaOcupadaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VallaOcupadaDao {
    @Query("SELECT * FROM VallasOcupadas")
    fun observerAll(): Flow<List<VallaOcupadaEntity>>

    @Upsert
    suspend fun upsertAll(vallas: List<VallaOcupadaEntity>)

    @Query("DELETE FROM VallasOcupadas")
    suspend fun clearAll()
}