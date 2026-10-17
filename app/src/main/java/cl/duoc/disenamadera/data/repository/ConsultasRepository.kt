package cl.duoc.disenamadera.data.repository

import cl.duoc.disenamadera.data.local.dao.ConsultaTecnicaDao
import cl.duoc.disenamadera.data.local.toEntity
import cl.duoc.disenamadera.data.local.toModel
import cl.duoc.disenamadera.data.model.ConsultaTecnica
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConsultasRepository(private val dao: ConsultaTecnicaDao) {

    fun observarConsultas(): Flow<List<ConsultaTecnica>> =
        dao.observarConsultas().map { lista -> lista.map { it.toModel() } }

    suspend fun obtener(consultaId: Long): ConsultaTecnica? = dao.obtener(consultaId)?.toModel()

    /** Guarda la consulta (el "envío" es simulado). @return el id asignado. */
    suspend fun guardar(consulta: ConsultaTecnica): Long = dao.insertar(consulta.toEntity())
}
