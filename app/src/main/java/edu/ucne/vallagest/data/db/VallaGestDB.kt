package edu.ucne.vallagest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.vallagest.data.local.dao.*
import edu.ucne.vallagest.data.local.entities.*

@Database(
    entities = [
        VallaEntity::class,
        UsuarioEntity::class,
        CategoriaEntity::class,
        CarritoEntity::class,
        OrdenEntity::class,
        OrdenDetalleEntity::class,
        VallaOcupadaEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class VallaGestDb : RoomDatabase() {
    abstract fun vallaDao(): VallaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun carritoDao(): CarritoDao
    abstract fun ordenDao(): OrdenDao

    abstract fun vallaOcupadaDao(): VallaOcupadaDao
}