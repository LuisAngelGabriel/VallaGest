package edu.ucne.vallagest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.vallagest.data.local.dao.UsuarioDao
import edu.ucne.vallagest.data.local.dao.VallaDao
import edu.ucne.vallagest.data.local.entities.UsuarioEntity
import edu.ucne.vallagest.data.local.entities.VallaEntity

@Database(
    entities = [
        VallaEntity::class,
        UsuarioEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VallaGestDb : RoomDatabase() {
    abstract fun vallaDao(): VallaDao
    abstract fun usuarioDao(): UsuarioDao
}