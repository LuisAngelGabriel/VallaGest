package edu.ucne.vallagest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.vallagest.data.local.dao.CarritoDao
import edu.ucne.vallagest.data.local.dao.CategoriaDao
import edu.ucne.vallagest.data.local.dao.UsuarioDao
import edu.ucne.vallagest.data.local.dao.VallaDao
import edu.ucne.vallagest.data.local.entities.CategoriaEntity
import edu.ucne.vallagest.data.local.entities.UsuarioEntity
import edu.ucne.vallagest.data.local.entities.VallaEntity
import edu.ucne.vallagest.data.local.entities.CarritoEntity

@Database(
    entities = [
        VallaEntity::class,
        UsuarioEntity::class,
        CategoriaEntity::class,
        CarritoEntity::class

    ],
    version = 4,
    exportSchema = false
)
abstract class VallaGestDb : RoomDatabase() {
    abstract fun vallaDao(): VallaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun categoriaDao(): CategoriaDao

    abstract fun carritoDao(): CarritoDao
}