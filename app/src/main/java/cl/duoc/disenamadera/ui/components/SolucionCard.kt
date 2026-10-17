package cl.duoc.disenamadera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.disenamadera.data.model.Solucion
import coil.compose.AsyncImage

/** Tarjeta horizontal para listas: imagen a la izquierda, datos a la derecha. */
@Composable
fun SolucionCard(solucion: Solucion, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SolucionImagen(solucion.imagen, Modifier.size(80.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    solucion.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(solucion.sistemaConstructivo, style = MaterialTheme.typography.bodyMedium)
                Text(
                    solucion.material,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

/** Tarjeta vertical y más grande, pensada para carruseles de soluciones destacadas. */
@Composable
fun SolucionDestacadaCard(solucion: Solucion, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        onClick = onClick,
        modifier = modifier.width(240.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SolucionImagen(solucion.imagen, Modifier.fillMaxWidth().height(110.dp), redondeada = false)
            Column(
                modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    solucion.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(solucion.sistemaConstructivo, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/** Foto de la solución con Coil; si no tiene imagen, muestra un ícono de reemplazo. */
@Composable
private fun SolucionImagen(imagen: String?, modifier: Modifier, redondeada: Boolean = true) {
    val forma = if (redondeada) Modifier.clip(MaterialTheme.shapes.medium) else Modifier
    if (imagen != null) {
        AsyncImage(
            model = imagen,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.then(forma)
        )
    } else {
        Box(
            modifier = modifier
                .then(forma)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Home,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
