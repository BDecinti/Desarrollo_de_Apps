package cl.duoc.disenamadera.data.repository

import cl.duoc.disenamadera.data.local.dao.CatalogoDao
import cl.duoc.disenamadera.data.local.toModel
import cl.duoc.disenamadera.data.model.NormativaDetalle
import cl.duoc.disenamadera.data.model.OpcionesFiltro
import cl.duoc.disenamadera.data.model.RecursoDetalle
import cl.duoc.disenamadera.data.model.Solucion
import cl.duoc.disenamadera.data.model.SolucionDetalle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Única puerta de acceso al catálogo: los ViewModels piden aquí, nunca al DAO. */
class CatalogoRepository(private val dao: CatalogoDao) {

    fun observarSoluciones(): Flow<List<Solucion>> =
        dao.observarSoluciones().map { lista -> lista.map { it.toModel() } }

    fun observarDestacadas(): Flow<List<Solucion>> =
        dao.observarDestacadas().map { lista -> lista.map { it.toModel() } }

    /** @return la ficha completa, o null si el id no existe. */
    suspend fun obtenerDetalle(solucionId: Int): SolucionDetalle? {
        val solucion = dao.obtenerSolucion(solucionId) ?: return null
        return SolucionDetalle(
            solucion = solucion.toModel(),
            normativas = dao.normativasDe(solucionId).map { it.toModel() },
            recursos = dao.recursosDe(solucionId).map { it.toModel() },
            relacionadas = dao.relacionadasDe(solucionId).map { it.toModel() }
        )
    }

    suspend fun obtenerNormativa(normativaId: Int): NormativaDetalle? {
        val normativa = dao.obtenerNormativa(normativaId) ?: return null
        return NormativaDetalle(
            normativa = normativa.toModel(),
            soluciones = dao.solucionesDeNormativa(normativaId).map { it.toModel() }
        )
    }

    suspend fun obtenerRecurso(recursoId: Int): RecursoDetalle? {
        val recurso = dao.obtenerRecurso(recursoId) ?: return null
        return RecursoDetalle(
            recurso = recurso.toModel(),
            soluciones = dao.solucionesDeRecurso(recursoId).map { it.toModel() }
        )
    }

    suspend fun obtenerOpcionesFiltro() = OpcionesFiltro(
        sistemasConstructivos = dao.sistemasConstructivos(),
        materiales = dao.materiales(),
        areasTematicas = dao.areasTematicas(),
        aplicaciones = dao.aplicaciones()
    )
}
