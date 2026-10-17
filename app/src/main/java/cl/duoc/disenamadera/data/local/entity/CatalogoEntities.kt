package cl.duoc.disenamadera.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Entidades = tablas de Room. Una clase por tabla.

@Entity(tableName = "solucion")
data class SolucionEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val sistemaConstructivo: String,
    val material: String,
    val areaTematica: String,
    val aplicacion: String,
    val imagen: String?,
    val destacada: Boolean
)

@Entity(tableName = "normativa")
data class NormativaEntity(
    @PrimaryKey val id: Int,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val url: String
)

@Entity(tableName = "recurso")
data class RecursoEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    /** Nombre del enum TipoRecurso (DOCUMENTO o HERRAMIENTA). */
    val tipo: String,
    val descripcion: String,
    val url: String
)

// Tablas cruzadas: representan relaciones muchos-a-muchos entre soluciones y otras cosas.

@Entity(
    tableName = "solucion_normativa",
    primaryKeys = ["solucionId", "normativaId"],
    foreignKeys = [
        ForeignKey(SolucionEntity::class, ["id"], ["solucionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(NormativaEntity::class, ["id"], ["normativaId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("normativaId")]
)
data class SolucionNormativaCrossRef(val solucionId: Int, val normativaId: Int)

@Entity(
    tableName = "solucion_recurso",
    primaryKeys = ["solucionId", "recursoId"],
    foreignKeys = [
        ForeignKey(SolucionEntity::class, ["id"], ["solucionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(RecursoEntity::class, ["id"], ["recursoId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("recursoId")]
)
data class SolucionRecursoCrossRef(val solucionId: Int, val recursoId: Int)

@Entity(
    tableName = "solucion_relacionada",
    primaryKeys = ["solucionId", "relacionadaId"],
    foreignKeys = [
        ForeignKey(SolucionEntity::class, ["id"], ["solucionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(SolucionEntity::class, ["id"], ["relacionadaId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("relacionadaId")]
)
data class SolucionRelacionadaCrossRef(val solucionId: Int, val relacionadaId: Int)
