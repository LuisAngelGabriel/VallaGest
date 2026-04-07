package edu.ucne.vallagest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.ucne.vallagest.data.local.entities.CarritoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarritoDao {
    @Query("SELECT * FROM CarritoItems")
    fun getCarritoLocal(): Flow<List<CarritoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarrito(items: List<CarritoEntity>)

    @Query("DELETE FROM CarritoItems WHERE carritoItemId = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM CarritoItems")
    suspend fun clearCarrito()
}