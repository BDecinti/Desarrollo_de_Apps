package cl.duoc.disenamadera.data.repository

import cl.duoc.disenamadera.data.local.dao.FavoritoDao
import cl.duoc.disenamadera.data.local.entity.FavoritoEntity
import cl.duoc.disenamadera.data.local.toModel
import cl.duoc.disenamadera.data.model.Solucion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritosRepository(
    private val dao: FavoritoDao,
    private val reloj: () -> Long = System::currentTimeMillis
) {
    /** Soluciones favoritas, la última marcada primero. */
    fun observarFavoritos(): Flow<List<Solucion>> =
        dao.observarFavoritos().map { lista -> lista.map { it.solucion.toModel() } }

    /** Ids favoritos, útil para pintar el corazón en cada tarjeta de una lista. */
    fun observarIds(): Flow<Set<Int>> = dao.observarIds().map { it.toSet() }

    fun observarEsFavorito(solucionId: Int): Flow<Boolean> = dao.observarEsFavorito(solucionId)

    /** Marca la solución como favorita si no lo era; si ya lo era, la quita. */
    suspend fun alternar(solucionId: Int) {
        if (dao.esFavorito(solucionId)) {
            dao.eliminar(solucionId)
        } else {
            dao.insertar(FavoritoEntity(solucionId, reloj()))
        }
    }

    suspend fun quitar(solucionId: Int) = dao.eliminar(solucionId)

    suspend fun limpiar() = dao.eliminarTodos()
}
