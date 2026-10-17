package cl.duoc.disenamadera.data.model

// Modelos de dominio del catálogo: son lo único que ven los ViewModels y las pantallas.
// No conocen Room ni JSON (eso vive en data/local).

data class Solucion(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val sistemaConstructivo: String,
    val material: String,
    val areaTematica: String,
    val aplicacion: String,
    val imagen: String?,
    val destacada: Boolean
)

data class Normativa(
    val id: Int,
    val codigo: String,
    val titulo: String,
    val descripcion: String,
    val url: String
)

enum class TipoRecurso { DOCUMENTO, HERRAMIENTA }

data class Recurso(
    val id: Int,
    val titulo: String,
    val tipo: TipoRecurso,
    val descripcion: String,
    val url: String
)

/** Ficha completa: la solución más todo lo que tiene relacionado. */
data class SolucionDetalle(
    val solucion: Solucion,
    val normativas: List<Normativa>,
    val recursos: List<Recurso>,
    val relacionadas: List<Solucion>
)

data class NormativaDetalle(
    val normativa: Normativa,
    val soluciones: List<Solucion>
)

data class RecursoDetalle(
    val recurso: Recurso,
    val soluciones: List<Solucion>
)

/** Valores disponibles para cada filtro, tomados del catálogo real. */
data class OpcionesFiltro(
    val sistemasConstructivos: List<String>,
    val materiales: List<String>,
    val areasTematicas: List<String>,
    val aplicaciones: List<String>
)
