package cl.duoc.disenamadera.ui.screens.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.disenamadera.R

/**
 * Bienvenida: muestra el nombre de la app mientras se precarga el catálogo en Room.
 * Si la carga falla, ofrece reintentar en lugar de dejar la app en blanco.
 */
@Composable
fun SplashScreen(
    esperarCatalogo: suspend () -> Boolean,
    onListo: () -> Unit,
    modifier: Modifier = Modifier
) {
    var intento by remember { mutableIntStateOf(0) }
    var hayError by remember { mutableStateOf(false) }

    // Se vuelve a ejecutar cada vez que cambia "intento" (al pulsar Reintentar)
    LaunchedEffect(intento) {
        hayError = false
        if (esperarCatalogo()) onListo() else hayError = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Text(
            stringResource(R.string.splash_subtitulo),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        if (hayError) {
            Text(
                stringResource(R.string.splash_error),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Button(onClick = { intento++ }) { Text(stringResource(R.string.reintentar)) }
        } else {
            CircularProgressIndicator()
            Text(stringResource(R.string.splash_cargando), style = MaterialTheme.typography.bodySmall)
        }
    }
}
