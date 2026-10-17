package cl.duoc.disenamadera.ui.screens.explorar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.disenamadera.data.model.Solucion
import cl.duoc.disenamadera.data.repository.CatalogoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/** Todo lo que la pantalla Explorar necesita para dibujarse; la pantalla no calcula nada por su cuenta. */
data class ExplorarUiState(
    val cargando: Boolean = true,
    val hayError: Boolean = false,
    val consulta: String = "",
    val destacadas: List<Solucion> = emptyList(),
    val soluciones: List<Solucion> = emptyList(),
    val areas: List<String> = emptyList()
)

class ExplorarViewModel(catalogo: CatalogoRepository) : ViewModel() {

    private val consulta = MutableStateFlow("")

    val uiState: StateFlow<ExplorarUiState> = combine(
        catalogo.observarSoluciones(),
        catalogo.observarDestacadas(),
        consulta
    ) { soluciones, destacadas, texto ->
        ExplorarUiState(
            cargando = false,
            consulta = texto,
            destacadas = destacadas,
            soluciones = soluciones,
            // Las categorías salen de los datos reales, no de una lista escrita a mano
            areas = soluciones.map { it.areaTematica }.distinct().sorted()
        )
    }
        .catch { emit(ExplorarUiState(cargando = false, hayError = true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ExplorarUiState())

    fun onConsultaChange(texto: String) {
        consulta.value = texto.take(LARGO_MAXIMO_BUSQUEDA)
    }

    private companion object {
        const val LARGO_MAXIMO_BUSQUEDA = 60
    }
}
