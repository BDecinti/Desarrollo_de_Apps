package cl.duoc.disenamadera.data.local.seed

import cl.duoc.disenamadera.data.local.dao.CatalogoDao
import cl.duoc.disenamadera.data.local.entity.NormativaEntity
import cl.duoc.disenamadera.data.local.entity.RecursoEntity
import cl.duoc.disenamadera.data.local.entity.SolucionEntity
import cl.duoc.disenamadera.data.local.entity.SolucionNormativaCrossRef
import cl.duoc.disenamadera.data.local.entity.SolucionRecursoCrossRef
import cl.duoc.disenamadera.data.local.entity.SolucionRelacionadaCrossRef
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// Formato de assets/catalogo.json. Las relaciones van como listas de ids dentro de cada solución.

@Serializable
data class CatalogoJson(
    val soluciones: List<SolucionJson>,
    val normativas: List<NormativaJson>,
    val recursos: List<RecursoJson>
)

@Serializable
data class SolucionJson(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val sistemaConstructivo: String,
    val material: String,
    val areaTematica: String,
    val aplicacion: String,
    val imagen: String? = null,
    val destacada: Boolean = false,
    val normativas: List<Int> = emptyList(),
    val recursos: List<Int> = emptyList(),
    val relacionadas: List<Int> = emptyList()
)

@Serializable
data class NormativaJson(
    val id: Int,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val url: String
)

@Serializable
data class RecursoJson(
    val id: Int,
    val titulo: String,
    val tipo: String,
    val descripcion: String,
    val url: String
)

/**
 * Lee el JSON del catálogo y lo guarda en Room la primera vez.
 * Recibe una función que entrega el texto del JSON (en la app viene de assets/),
 * así la clase no depende de Android y se puede probar con un texto cualquiera.
 */
class CatalogoSeeder(
    private val dao: CatalogoDao,
    private val leerJson: () -> String
) {
    private val json = Json { ignoreUnknownKeys = true }

    /** @return true si cargó el catálogo, false si ya estaba cargado. */
    suspend fun sembrarSiHaceFalta(): Boolean {
        val catalogo = json.decodeFromString<CatalogoJson>(leerJson())
        return dao.sembrarSiVacio(
            soluciones = catalogo.soluciones.map {
                SolucionEntity(
                    it.id, it.nombre, it.descripcion, it.sistemaConstructivo,
                    it.material, it.areaTematica, it.aplicacion, it.imagen, it.destacada
                )
            },
            normativas = catalogo.normativas.map {
                NormativaEntity(it.id, it.codigo, it.titulo, it.descripcion, it.url)
            },
            recursos = catalogo.recursos.map {
                RecursoEntity(it.id, it.titulo, it.tipo, it.descripcion, it.url)
            },
            solucionNormativas = catalogo.soluciones.flatMap { s ->
                s.normativas.map { SolucionNormativaCrossRef(s.id, it) }
            },
            solucionRecursos = catalogo.soluciones.flatMap { s ->
                s.recursos.map { SolucionRecursoCrossRef(s.id, it) }
            },
            solucionRelacionadas = catalogo.soluciones.flatMap { s ->
                s.relacionadas.map { SolucionRelacionadaCrossRef(s.id, it) }
            }
        )
    }
}
