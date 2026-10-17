package cl.duoc.disenamadera.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.duoc.disenamadera.DisenaMaderaApp
import cl.duoc.disenamadera.R

/**
 * Armazón de la app: barra superior + barra inferior + el contenido que cambia según la ruta.
 * La barra inferior solo aparece en los 4 destinos principales; la bienvenida no lleva ninguna barra.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(app: DisenaMaderaApp) {
    val navController = rememberNavController()
    val entrada by navController.currentBackStackEntryAsState()
    val destino = entrada?.destination

    val esSplash = destino.es<RutaSplash>()
    val principal = DestinosPrincipales.firstOrNull { destino.esClase(it) }

    Scaffold(
        topBar = {
            if (!esSplash) {
                TopAppBar(
                    title = { Text(titulo(destino, principal)) },
                    navigationIcon = {
                        // Los destinos principales no tienen "atrás"; los secundarios sí
                        if (principal == null) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(R.string.volver)
                                )
                            }
                        }
                    },
                    actions = {
                        if (principal != null) {
                            MenuSuperior(onAcercaClick = { navController.navigate(RutaAcerca) })
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (principal != null) {
                NavigationBar {
                    DestinosPrincipales.forEach { item ->
                        NavigationBarItem(
                            selected = destino.esClase(item),
                            onClick = {
                                navController.navigate(item.ruta) {
                                    // Vuelve a Explorar antes de abrir otra pestaña, guardando su estado,
                                    // para que "atrás" no acumule pestañas y cada una recuerde dónde estaba
                                    popUpTo<RutaExplorar> { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icono, contentDescription = null) },
                            label = { Text(stringResource(item.etiqueta)) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        AppNavGraph(navController, app, Modifier.padding(padding))
    }
}

@Composable
private fun MenuSuperior(onAcercaClick: () -> Unit) {
    var abierto by remember { mutableStateOf(false) }
    IconButton(onClick = { abierto = true }) {
        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.mas_opciones))
    }
    DropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.menu_acerca)) },
            onClick = {
                abierto = false
                onAcercaClick()
            }
        )
    }
}

@Composable
private fun titulo(destino: NavDestination?, principal: DestinoPrincipal?): String = when {
    principal != null -> stringResource(principal.etiqueta)
    destino.es<RutaAcerca>() -> stringResource(R.string.menu_acerca)
    destino.es<RutaResultados>() -> stringResource(R.string.titulo_resultados)
    destino.es<RutaDetalleSolucion>() -> stringResource(R.string.titulo_detalle)
    else -> stringResource(R.string.app_name)
}

private inline fun <reified T : Any> NavDestination?.es(): Boolean =
    this?.hierarchy?.any { it.hasRoute<T>() } == true

private fun NavDestination?.esClase(item: DestinoPrincipal): Boolean =
    this?.hierarchy?.any { it.hasRoute(item.clase) } == true
