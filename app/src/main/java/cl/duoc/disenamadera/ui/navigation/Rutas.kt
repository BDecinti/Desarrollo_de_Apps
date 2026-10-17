package cl.duoc.disenamadera.ui.navigation

import kotlinx.serialization.Serializable

// Rutas con tipo (type-safe): cada pantalla es una clase u objeto, no un texto suelto como "detalle/3".
// Si una ruta lleva datos (ej. el id de la solución), van como propiedades y el compilador los revisa.

@Serializable data object RutaSplash
@Serializable data object RutaExplorar
@Serializable data object RutaFavoritos
@Serializable data object RutaHistorial
@Serializable data object RutaConsulta
@Serializable data object RutaAcerca

/** Se abre con un texto de búsqueda, con un área temática (acceso por categoría), o con ambos. */
@Serializable data class RutaResultados(val texto: String = "", val area: String? = null)

@Serializable data class RutaDetalleSolucion(val id: Int)
