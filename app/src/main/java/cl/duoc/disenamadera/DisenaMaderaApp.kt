package cl.duoc.disenamadera

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

/** Se crea una sola vez al abrir la app, antes que cualquier pantalla. */
class DisenaMaderaApp : Application() {

    lateinit var container: AppContainer
        private set

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var cargaCatalogo: Deferred<Result<Unit>>

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        cargaCatalogo = lanzarCarga()
    }

    /**
     * Espera a que el catálogo esté en Room (lo usa la pantalla de bienvenida).
     * Si la carga falló, la vuelve a intentar al llamarla de nuevo.
     * @return true si el catálogo quedó disponible.
     */
    suspend fun esperarCatalogo(): Boolean {
        if (cargaCatalogo.await().isSuccess) return true
        cargaCatalogo = lanzarCarga()
        return cargaCatalogo.await().isSuccess
    }

    private fun lanzarCarga() = scope.async { runCatching { container.inicializarCatalogo() } }
}
