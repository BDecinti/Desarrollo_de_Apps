package cl.duoc.disenamadera.data.model

// Modelos de los datos que genera el usuario al usar la app.

/** Una ficha abierta anteriormente, con la fecha (milisegundos epoch) en que se consultó. */
data class EntradaHistorial(
    val id: Long,
    val solucion: Solucion,
    val fecha: Long
)

/** Consulta técnica enviada desde el formulario. El "envío" es simulado: solo se guarda en local. */
data class ConsultaTecnica(
    val id: Long = 0,
    val nombre: String,
    val correo: String,
    val telefono: String?,
    val perfil: String,
    val solucionId: Int?,
    val mensaje: String,
    val fotoUri: String?,
    val fecha: Long
)
