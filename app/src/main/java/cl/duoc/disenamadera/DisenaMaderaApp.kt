package cl.duoc.disenamadera

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Se crea una sola vez al abrir la app, antes que cualquier pantalla. */
class DisenaMaderaApp : Application() {

    lateinit var container: AppContainer
        private set

    /** Trabajo de carga del catálogo; la pantalla de bienvenida (etapa 3) puede esperarlo con join(). */
    lateinit var cargaCatalogo: Job
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        cargaCatalogo = CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            container.inicializarCatalogo()
        }
    }
}
