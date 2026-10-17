package cl.duoc.disenamadera

import android.content.Context
import cl.duoc.disenamadera.data.local.DisenaMaderaDatabase
import cl.duoc.disenamadera.data.local.seed.CatalogoSeeder
import cl.duoc.disenamadera.data.repository.CatalogoRepository
import cl.duoc.disenamadera.data.repository.ConsultasRepository
import cl.duoc.disenamadera.data.repository.FavoritosRepository
import cl.duoc.disenamadera.data.repository.HistorialRepository

/**
 * Contenedor de dependencias hecho a mano: crea la base de datos y los repositorios una sola vez
 * y los entrega a quien los necesite (en la etapa 3, a los ViewModels mediante una factory).
 */
class AppContainer(context: Context) {
    private val database = DisenaMaderaDatabase.crear(context)

    val catalogoRepository = CatalogoRepository(database.catalogoDao())
    val favoritosRepository = FavoritosRepository(database.favoritoDao())
    val historialRepository = HistorialRepository(database.historialDao())
    val consultasRepository = ConsultasRepository(database.consultaTecnicaDao())

    private val seeder = CatalogoSeeder(database.catalogoDao()) {
        context.applicationContext.assets.open("catalogo.json").bufferedReader().use { it.readText() }
    }

    /** Carga assets/catalogo.json en Room si aún no se ha hecho. Es seguro llamarla muchas veces. */
    suspend fun inicializarCatalogo() {
        seeder.sembrarSiHaceFalta()
    }
}
