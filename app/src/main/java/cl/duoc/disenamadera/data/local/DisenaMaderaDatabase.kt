package cl.duoc.disenamadera.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.disenamadera.data.local.dao.CatalogoDao
import cl.duoc.disenamadera.data.local.dao.ConsultaTecnicaDao
import cl.duoc.disenamadera.data.local.dao.FavoritoDao
import cl.duoc.disenamadera.data.local.dao.HistorialDao
import cl.duoc.disenamadera.data.local.entity.ConsultaTecnicaEntity
import cl.duoc.disenamadera.data.local.entity.FavoritoEntity
import cl.duoc.disenamadera.data.local.entity.HistorialEntity
import cl.duoc.disenamadera.data.local.entity.NormativaEntity
import cl.duoc.disenamadera.data.local.entity.RecursoEntity
import cl.duoc.disenamadera.data.local.entity.SolucionEntity
import cl.duoc.disenamadera.data.local.entity.SolucionNormativaCrossRef
import cl.duoc.disenamadera.data.local.entity.SolucionRecursoCrossRef
import cl.duoc.disenamadera.data.local.entity.SolucionRelacionadaCrossRef

/** La base de datos de la app: junta todas las tablas y entrega los DAOs. */
@Database(
    entities = [
        SolucionEntity::class,
        NormativaEntity::class,
        RecursoEntity::class,
        SolucionNormativaCrossRef::class,
        SolucionRecursoCrossRef::class,
        SolucionRelacionadaCrossRef::class,
        FavoritoEntity::class,
        HistorialEntity::class,
        ConsultaTecnicaEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class DisenaMaderaDatabase : RoomDatabase() {
    abstract fun catalogoDao(): CatalogoDao
    abstract fun favoritoDao(): FavoritoDao
    abstract fun historialDao(): HistorialDao
    abstract fun consultaTecnicaDao(): ConsultaTecnicaDao

    companion object {
        private const val NOMBRE = "disena_madera.db"

        fun crear(context: Context): DisenaMaderaDatabase =
            Room.databaseBuilder(context.applicationContext, DisenaMaderaDatabase::class.java, NOMBRE).build()
    }
}
