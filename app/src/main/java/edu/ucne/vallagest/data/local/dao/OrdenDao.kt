package edu.ucne.vallagest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.ucne.vallagest.data.local.entities.OrdenEntity
import edu.ucne.vallagest.data.local.entities.OrdenDetalleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrden(orden: OrdenEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetalles(detalles: List<OrdenDetalleEntity>)

    @Query("SELECT * FROM Ordenes WHERE usuarioId = :usuarioId ORDER BY fechaOrden DESC")
    fun getOrdenes(usuarioId: Int): Flow<List<OrdenEntity>>

    @Query("SELECT * FROM OrdenesDetalles WHERE ordenId = :ordenId")
    suspend fun getDetallesByOrden(ordenId: Int): List<OrdenDetalleEntity>

    @Query("DELETE FROM Ordenes WHERE usuarioId = :usuarioId")
    suspend fun clearOrdenes(usuarioId: Int)
}