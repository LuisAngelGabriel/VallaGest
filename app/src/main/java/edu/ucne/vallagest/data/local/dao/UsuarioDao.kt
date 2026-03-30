package edu.ucne.vallagest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import edu.ucne.vallagest.data.local.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM Usuarios WHERE estaLogueado = 1 LIMIT 1")
    fun getLoggedUsuario(): Flow<UsuarioEntity?>

    @Query("UPDATE Usuarios SET estaLogueado = 0")
    suspend fun logout()

    @Query("DELETE FROM Usuarios")
    suspend fun clearAll()
}