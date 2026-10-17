package cl.duoc.disenamadera.data.repository

import cl.duoc.disenamadera.data.local.dao.HistorialDao
import cl.duoc.disenamadera.data.local.toModel
import cl.duoc.disenamadera.data.model.EntradaHistorial
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistorialRepository(
    private val dao: HistorialDao,
    private val reloj: () -> Long = System::currentTimeMillis
) {
    /** Últimas fichas consultadas, de la más reciente a la más antigua. */
    fun observarHistorial(): Flow<List<EntradaHistorial>> =
        dao.observarHistorial(LIMITE).map { lista -> lista.map { it.toModel() } }

    /** Anota que se abrió una ficha (no repite si es la misma que la última vista). */
    suspend fun registrar(solucionId: Int) = dao.registrarSinDuplicarConsecutivo(solucionId, reloj())

    suspend fun limpiar() = dao.eliminarTodo()

    private companion object {
        const val LIMITE = 50
    }
}
