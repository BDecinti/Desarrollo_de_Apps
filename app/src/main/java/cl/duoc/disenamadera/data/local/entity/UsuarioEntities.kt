package cl.duoc.disenamadera.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

// Tablas con los datos del usuario. Las fechas se guardan como milisegundos epoch.

/** Una fila por solución favorita; la clave primaria evita marcar dos veces la misma. */
@Entity(
    tableName = "favorito",
    foreignKeys = [ForeignKey(SolucionEntity::class, ["id"], ["solucionId"], onDelete = ForeignKey.CASCADE)]
)
data class FavoritoEntity(
    @PrimaryKey val solucionId: Int,
    val fecha: Long
)

@Entity(
    tableName = "historial",
    foreignKeys = [ForeignKey(SolucionEntity::class, ["id"], ["solucionId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("solucionId")]
)
data class HistorialEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val solucionId: Int,
    val fecha: Long
)

@Entity(
    tableName = "consulta_tecnica",
    foreignKeys = [ForeignKey(SolucionEntity::class, ["id"], ["solucionId"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("solucionId")]
)
data class ConsultaTecnicaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nombre: String,
    val correo: String,
    val telefono: String?,
    val perfil: String,
    val solucionId: Int?,
    val mensaje: String,
    val fotoUri: String?,
    val fecha: Long
)

// Resultados de consultas que traen la solución junto con el registro del usuario.

data class FavoritoConSolucion(
    @Embedded val favorito: FavoritoEntity,
    @Relation(parentColumn = "solucionId", entityColumn = "id") val solucion: SolucionEntity
)

data class HistorialConSolucion(
    @Embedded val historial: HistorialEntity,
    @Relation(parentColumn = "solucionId", entityColumn = "id") val solucion: SolucionEntity
)
