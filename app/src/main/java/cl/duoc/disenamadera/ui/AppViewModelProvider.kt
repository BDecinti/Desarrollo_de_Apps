package cl.duoc.disenamadera.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.duoc.disenamadera.AppContainer
import cl.duoc.disenamadera.ui.screens.explorar.ExplorarViewModel

/**
 * Fábricas de ViewModels. Android no sabe construir un ViewModel que recibe parámetros,
 * así que aquí le explicamos cómo: tomando los repositorios del AppContainer.
 * Cada etapa nueva agrega aquí su ViewModel.
 */
object AppViewModelProvider {
    fun explorar(container: AppContainer) = viewModelFactory {
        initializer { ExplorarViewModel(container.catalogoRepository) }
    }
}
