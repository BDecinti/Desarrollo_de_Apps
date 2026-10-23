package com.example.myapplication.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.device.rememberLocationAction
import com.example.myapplication.domain.climate.ClimateZone

/**
 * Tarjeta que usa el recurso nativo de UBICACIÓN para detectar la zona climática del usuario
 * y recomendar maderas acordes (se integra con el filtro de clima de la pantalla de Muebles).
 */
@Composable
fun ClimateZoneCard(
    zone: ClimateZone?,
    activeClimateFilter: String,
    onLocationDetected: (latitude: Double, longitude: Double) -> Boolean,
    onApplyZoneFilter: (ClimateZone) -> Unit,
    modifier: Modifier = Modifier
) {
    var isLocating by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val detectLocation = rememberLocationAction(
        onLoadingChange = { isLocating = it },
        onLocation = { latitude, longitude ->
            val detected = ClimateZone.fromCoordinates(latitude, longitude)
            val saved = onLocationDetected(latitude, longitude)
            if (saved && detected != null) {
                message = null
                onApplyZoneFilter(detected) // el filtro de clima se ajusta automáticamente
            } else {
                message = "Tu ubicación está fuera de Chile, por eso mostramos todas las recomendaciones."
            }
        },
        onError = { message = it }
    )

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape)
                ) {
                    Icon(
                        imageVector = zoneIcon(zone),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                AnimatedContent(
                    targetState = zone,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "ZoneText",
                    modifier = Modifier.weight(1f)
                ) { currentZone ->
                    Column {
                        Text(
                            text = if (currentZone != null) "${currentZone.region} · ${currentZone.label}"
                            else "Tu zona climática",
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = currentZone?.resumen
                                ?: "Detecta tu ubicación y te mostramos qué madera conviene según el clima donde vives.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = message != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = message.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = {
                        message = null
                        detectLocation()
                    },
                    enabled = !isLocating,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLocating) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Buscando...")
                    } else {
                        Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (zone == null) "Detectar mi zona" else "Actualizar")
                    }
                }

                AnimatedVisibility(visible = zone != null && activeClimateFilter != zone.label) {
                    TextButton(onClick = { zone?.let(onApplyZoneFilter) }) {
                        Text("Filtrar por mi zona")
                    }
                }
            }
        }
    }
}

private fun zoneIcon(zone: ClimateZone?): ImageVector = when (zone) {
    ClimateZone.CALIDO_SECO -> Icons.Default.LightMode
    ClimateZone.TEMPLADO -> Icons.Default.Home
    ClimateZone.HUMEDO -> Icons.Default.WaterDrop
    null -> Icons.Default.Place
}
