package cl.duoc.disenamadera.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cl.duoc.disenamadera.data.local.entity.NormativaEntity
import cl.duoc.disenamadera.data.local.entity.RecursoEntity
import cl.duoc.disenamadera.data.local.entity.SolucionEntity
import cl.duoc.disenamadera.data.local.entity.SolucionNormativaCrossRef
import cl.duoc.disenamadera.data.local.entity.SolucionRecursoCrossRef
import cl.duoc.disenamadera.data.local.entity.SolucionRelacionadaCrossRef
import kotlinx.coroutines.flow.Flow

/** Consultas del catálogo (soluciones, normativa, recursos y sus relaciones). */
@Dao
abstract class CatalogoDao {

    // --- Soluciones ---

    @Query("SELECT * FROM solucion ORDER BY nombre")
    abstract fun observarSoluciones(): Flow<List<SolucionEntity>>

    @Query("SELECT * FROM solucion WHERE destacada = 1 ORDER BY nombre")
    abstract fun observarDestacadas(): Flow<List<SolucionEntity>>

    @Query("SELECT * FROM solucion WHERE id = :id")
    abstract suspend fun obtenerSolucion(id: Int): SolucionEntity?

    @Query("SELECT COUNT(*) FROM solucion")
    abstract suspend fun contarSoluciones(): Int

    // --- Lo relacionado con una solución ---

    @Query(
        "SELECT n.* FROM normativa n " +
            "INNER JOIN solucion_normativa sn ON sn.normativaId = n.id " +
            "WHERE sn.solucionId = :solucionId ORDER BY n.codigo"
    )
    abstract suspend fun normativasDe(solucionId: Int): List<NormativaEntity>

    @Query(
        "SELECT r.* FROM recurso r " +
            "INNER JOIN solucion_recurso sr ON sr.recursoId = r.id " +
            "WHERE sr.solucionId = :solucionId ORDER BY r.titulo"
    )
    abstract suspend fun recursosDe(solucionId: Int): List<RecursoEntity>

    @Query(
        "SELECT s.* FROM solucion s " +
            "INNER JOIN solucion_relacionada rel ON rel.relacionadaId = s.id " +
            "WHERE rel.solucionId = :solucionId ORDER BY s.nombre"
    )
    abstract suspend fun relacionadasDe(solucionId: Int): List<SolucionEntity>

    // --- Normativa y recursos ---

    @Query("SELECT * FROM normativa WHERE id = :id")
    abstract suspend fun obtenerNormativa(id: Int): NormativaEntity?

    @Query("SELECT * FROM recurso WHERE id = :id")
    abstract suspend fun obtenerRecurso(id: Int): RecursoEntity?

    @Query(
        "SELECT s.* FROM solucion s " +
            "INNER JOIN solucion_normativa sn ON sn.solucionId = s.id " +
            "WHERE sn.normativaId = :normativaId ORDER BY s.nombre"
    )
    abstract suspend fun solucionesDeNormativa(normativaId: Int): List<SolucionEntity>

    @Query(
        "SELECT s.* FROM solucion s " +
            "INNER JOIN solucion_recurso sr ON sr.solucionId = s.id " +
            "WHERE sr.recursoId = :recursoId ORDER BY s.nombre"
    )
    abstract suspend fun solucionesDeRecurso(recursoId: Int): List<SolucionEntity>

    // --- Valores disponibles para los filtros ---

    @Query("SELECT DISTINCT sistemaConstructivo FROM solucion ORDER BY sistemaConstructivo")
    abstract suspend fun sistemasConstructivos(): List<String>

    @Query("SELECT DISTINCT material FROM solucion ORDER BY material")
    abstract suspend fun materiales(): List<String>

    @Query("SELECT DISTINCT areaTematica FROM solucion ORDER BY areaTematica")
    abstract suspend fun areasTematicas(): List<String>

    @Query("SELECT DISTINCT aplicacion FROM solucion ORDER BY aplicacion")
    abstract suspend fun aplicaciones(): List<String>

    // --- Carga inicial (seed) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarSoluciones(soluciones: List<SolucionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarNormativas(normativas: List<NormativaEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarRecursos(recursos: List<RecursoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarSolucionNormativas(refs: List<SolucionNormativaCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarSolucionRecursos(refs: List<SolucionRecursoCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertarSolucionRelacionadas(refs: List<SolucionRelacionadaCrossRef>)

    /**
     * Inserta todo el catálogo en una sola transacción, y solo si la tabla está vacía.
     * Así, abrir la app muchas veces (o dos veces a la vez) nunca duplica los datos.
     * @return true si cargó el catálogo, false si ya estaba cargado.
     */
    @Transaction
    open suspend fun sembrarSiVacio(
        soluciones: List<SolucionEntity>,
        normativas: List<NormativaEntity>,
        recursos: List<RecursoEntity>,
        solucionNormativas: List<SolucionNormativaCrossRef>,
        solucionRecursos: List<SolucionRecursoCrossRef>,
        solucionRelacionadas: List<SolucionRelacionadaCrossRef>
    ): Boolean {
        if (contarSoluciones() > 0) return false
        // Primero las tablas "padre", después las cruzadas que apuntan a ellas (claves foráneas)
        insertarSoluciones(soluciones)
        insertarNormativas(normativas)
        insertarRecursos(recursos)
        insertarSolucionNormativas(solucionNormativas)
        insertarSolucionRecursos(solucionRecursos)
        insertarSolucionRelacionadas(solucionRelacionadas)
        return true
    }
}
