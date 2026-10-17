package cl.duoc.disenamadera.ui.screens.explorar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import cl.duoc.disenamadera.R
import cl.duoc.disenamadera.ui.components.EstadoCarga
import cl.duoc.disenamadera.ui.components.EstadoError
import cl.duoc.disenamadera.ui.components.EstadoVacio
import cl.duoc.disenamadera.ui.components.SolucionCard
import cl.duoc.disenamadera.ui.components.SolucionDestacadaCard

/**
 * Pantalla sin lógica: recibe el estado ya preparado y avisa con callbacks lo que hace el usuario.
 * Navegar y buscar lo decide quien la llama (el grafo de navegación y el ViewModel).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorarScreen(
    uiState: ExplorarUiState,
    onConsultaChange: (String) -> Unit,
    onBuscar: (String) -> Unit,
    onCategoriaClick: (String) -> Unit,
    onSolucionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.cargando -> EstadoCarga(modifier)
        uiState.hayError -> EstadoError(
            stringResource(R.string.estado_error_titulo),
            stringResource(R.string.estado_error_mensaje),
            modifier
        )
        uiState.soluciones.isEmpty() -> EstadoVacio(
            stringResource(R.string.estado_vacio_titulo),
            stringResource(R.string.estado_vacio_mensaje),
            modifier
        )
        else -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                BarraBusqueda(uiState.consulta, onConsultaChange, onBuscar)
            }
            item {
                Titulo(stringResource(R.string.explorar_categorias))
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(uiState.areas) { area ->
                        SuggestionChip(onClick = { onCategoriaClick(area) }, label = { Text(area) })
                    }
                }
            }
            if (uiState.destacadas.isNotEmpty()) {
                item {
                    Titulo(stringResource(R.string.explorar_destacadas))
                }
                item {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(uiState.destacadas, key = { it.id }) { solucion ->
                            SolucionDestacadaCard(solucion, onClick = { onSolucionClick(solucion.id) })
                        }
                    }
                }
            }
            item {
                Titulo(stringResource(R.string.explorar_todas))
            }
            items(uiState.soluciones, key = { it.id }) { solucion ->
                SolucionCard(solucion, onClick = { onSolucionClick(solucion.id) })
            }
        }
    }
}

@Composable
private fun BarraBusqueda(
    consulta: String,
    onConsultaChange: (String) -> Unit,
    onBuscar: (String) -> Unit
) {
    val buscar = { if (consulta.isNotBlank()) onBuscar(consulta.trim()) }
    OutlinedTextField(
        value = consulta,
        onValueChange = onConsultaChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.explorar_buscar_hint)) },
        leadingIcon = {
            IconButton(onClick = buscar) {
                Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.explorar_buscar))
            }
        },
        trailingIcon = {
            if (consulta.isNotEmpty()) {
                IconButton(onClick = { onConsultaChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.explorar_limpiar_busqueda))
                }
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { buscar() })
    )
}

@Composable
private fun Titulo(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleLarge)
}
