package edu.ucne.vallagest.data.local.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ordenes")
data class OrdenEntity(
    @PrimaryKey val ordenId: Int,
    val usuarioId: Int,
    val fechaOrden: String,
    val total: Double,
    val metodo: String,
    val estado: String,
    val comprobanteUrl: String?
)

@Entity(tableName = "OrdenDetalles")
data class OrdenDetalleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ordenId: Int,
    val vallaId: Int,
    val nombreValla: String,
    val precioAplicado: Double,
    val meses: Int
)