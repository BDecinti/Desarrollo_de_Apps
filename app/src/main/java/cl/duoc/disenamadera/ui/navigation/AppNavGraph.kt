package cl.duoc.disenamadera.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cl.duoc.disenamadera.DisenaMaderaApp
import cl.duoc.disenamadera.R
import cl.duoc.disenamadera.ui.AppViewModelProvider
import cl.duoc.disenamadera.ui.components.PantallaEnConstruccion
import cl.duoc.disenamadera.ui.screens.explorar.ExplorarScreen
import cl.duoc.disenamadera.ui.screens.explorar.ExplorarViewModel
import cl.duoc.disenamadera.ui.screens.splash.SplashScreen

/** El "mapa del metro": declara qué pantalla corresponde a cada ruta y cómo se pasa de una a otra. */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    app: DisenaMaderaApp,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = RutaSplash, modifier = modifier) {

        composable<RutaSplash> {
            SplashScreen(
                esperarCatalogo = app::esperarCatalogo,
                onListo = {
                    // inclusive = true: la bienvenida sale de la pila y "atrás" no vuelve a ella
                    navController.navigate(RutaExplorar) { popUpTo<RutaSplash> { inclusive = true } }
                }
            )
        }

        composable<RutaExplorar> {
            val viewModel: ExplorarViewModel = viewModel(
                factory = remember { AppViewModelProvider.explorar(app.container) }
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ExplorarScreen(
                uiState = uiState,
                onConsultaChange = viewModel::onConsultaChange,
                onBuscar = { texto -> navController.navigate(RutaResultados(texto = texto)) },
                onCategoriaClick = { area -> navController.navigate(RutaResultados(area = area)) },
                onSolucionClick = { id -> navController.navigate(RutaDetalleSolucion(id)) }
            )
        }

        // Pantallas aún sin construir: sirven para probar que la navegación y los argumentos funcionan
        composable<RutaResultados> { entrada ->
            val ruta = entrada.toRoute<RutaResultados>()
            val detalle = if (ruta.area != null) {
                stringResource(R.string.en_construccion_area, ruta.area)
            } else {
                stringResource(R.string.en_construccion_resultados, ruta.texto)
            }
            PantallaEnConstruccion(stringResource(R.string.titulo_resultados), detalle)
        }
        composable<RutaDetalleSolucion> { entrada ->
            val ruta = entrada.toRoute<RutaDetalleSolucion>()
            PantallaEnConstruccion(
                stringResource(R.string.titulo_detalle),
                stringResource(R.string.en_construccion_detalle, ruta.id)
            )
        }
        composable<RutaFavoritos> { PantallaEnConstruccion(stringResource(R.string.nav_favoritos)) }
        composable<RutaHistorial> { PantallaEnConstruccion(stringResource(R.string.nav_historial)) }
        composable<RutaConsulta> { PantallaEnConstruccion(stringResource(R.string.nav_consulta)) }
        composable<RutaAcerca> { PantallaEnConstruccion(stringResource(R.string.menu_acerca)) }
    }
}
