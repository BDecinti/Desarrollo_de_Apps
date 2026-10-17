package cl.duoc.disenamadera.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import cl.duoc.disenamadera.data.local.entity.ConsultaTecnicaEntity
import cl.duoc.disenamadera.data.local.entity.FavoritoConSolucion
import cl.duoc.disenamadera.data.local.entity.FavoritoEntity
import cl.duoc.disenamadera.data.local.entity.HistorialConSolucion
import cl.duoc.disenamadera.data.local.entity.HistorialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    @Transaction
    @Query("SELECT * FROM favorito ORDER BY fecha DESC")
    fun observarFavoritos(): Flow<List<FavoritoConSolucion>>

    @Query("SELECT solucionId FROM favorito")
    fun observarIds(): Flow<List<Int>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorito WHERE solucionId = :solucionId)")
    fun observarEsFavorito(solucionId: Int): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorito WHERE solucionId = :solucionId)")
    suspend fun esFavorito(solucionId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(favorito: FavoritoEntity)

    @Query("DELETE FROM favorito WHERE solucionId = :solucionId")
    suspend fun eliminar(solucionId: Int)

    @Query("DELETE FROM favorito")
    suspend fun eliminarTodos()
}

@Dao
abstract class HistorialDao {

    @Transaction
    @Query("SELECT * FROM historial ORDER BY fecha DESC, id DESC LIMIT :limite")
    abstract fun observarHistorial(limite: Int): Flow<List<HistorialConSolucion>>

    @Query("SELECT solucionId FROM historial ORDER BY fecha DESC, id DESC LIMIT 1")
    abstract suspend fun ultimaSolucionId(): Int?

    @Insert
    abstract suspend fun insertar(entrada: HistorialEntity)

    @Query("DELETE FROM historial")
    abstract suspend fun eliminarTodo()

    /** Registra la visita salvo que la última ficha vista sea la misma (sin duplicados consecutivos). */
    @Transaction
    open suspend fun registrarSinDuplicarConsecutivo(solucionId: Int, fecha: Long) {
        if (ultimaSolucionId() != solucionId) {
            insertar(HistorialEntity(solucionId = solucionId, fecha = fecha))
        }
    }
}

@Dao
interface ConsultaTecnicaDao {

    @Query("SELECT * FROM consulta_tecnica ORDER BY fecha DESC, id DESC")
    fun observarConsultas(): Flow<List<ConsultaTecnicaEntity>>

    @Query("SELECT * FROM consulta_tecnica WHERE id = :id")
    suspend fun obtener(id: Long): ConsultaTecnicaEntity?

    /** @return el id autogenerado de la nueva consulta. */
    @Insert
    suspend fun insertar(consulta: ConsultaTecnicaEntity): Long
}
