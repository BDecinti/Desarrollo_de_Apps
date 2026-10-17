package cl.duoc.disenamadera.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.ui.graphics.vector.ImageVector
import cl.duoc.disenamadera.R
import kotlin.reflect.KClass

/** Un botón de la barra inferior: a qué ruta lleva, qué texto y qué ícono muestra. */
data class DestinoPrincipal(
    val ruta: Any,
    val clase: KClass<out Any>,
    @StringRes val etiqueta: Int,
    val icono: ImageVector
)

val DestinosPrincipales = listOf(
    DestinoPrincipal(RutaExplorar, RutaExplorar::class, R.string.nav_explorar, Icons.Outlined.Explore),
    DestinoPrincipal(RutaFavoritos, RutaFavoritos::class, R.string.nav_favoritos, Icons.Outlined.FavoriteBorder),
    DestinoPrincipal(RutaHistorial, RutaHistorial::class, R.string.nav_historial, Icons.Outlined.History),
    DestinoPrincipal(RutaConsulta, RutaConsulta::class, R.string.nav_consulta, Icons.Outlined.Email)
)
