package cl.duoc.disenamadera.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// Los tres estados que puede tener cualquier lista: cargando, vacía o con error.

@Composable
fun EstadoCarga(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EstadoVacio(titulo: String, mensaje: String, modifier: Modifier = Modifier) {
    MensajeCentrado(titulo, mensaje, MaterialTheme.colorScheme.onSurface, modifier)
}

@Composable
fun EstadoError(titulo: String, mensaje: String, modifier: Modifier = Modifier) {
    MensajeCentrado(titulo, mensaje, MaterialTheme.colorScheme.error, modifier)
}

@Composable
private fun MensajeCentrado(
    titulo: String,
    mensaje: String,
    colorTitulo: androidx.compose.ui.graphics.Color,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo, style = MaterialTheme.typography.titleMedium, color = colorTitulo, textAlign = TextAlign.Center)
        Text(mensaje, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
    }
}
