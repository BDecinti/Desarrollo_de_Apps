package com.example.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.models.FichaTecnica

/**
 * Tarjeta interactiva para mostrar fichas técnicas.
 * Diseñada para escaneo rápido de atributos técnicos (Fuego, Térmico, Acústico).
 */
@Composable
fun FichaCard(
    ficha: FichaTecnica,
    onClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // --- ENCABEZADO: CÓDIGO + CATEGORÍA + BOTÓN FAVORITO ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge del Código de la Ficha
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = ficha.codigo,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }

                    // Badge de Categoría
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = ficha.categoria,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                // Botón de Favorito (animado)
                FavoriteButton(
                    isFavorite = ficha.esFavorito,
                    onClick = onFavoriteToggle,
                    buttonSize = 36.dp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- TÍTULO DE LA SOLUCIÓN ---
            Text(
                text = ficha.titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            // --- SISTEMA CONSTRUCTIVO Y MATERIAL ---
            Text(
                text = "${ficha.sistemaConstructivo} • ${ficha.material}",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- INDICADORES TÉCNICOS (FUEGO, TÉRMICO, ACÚSTICO) ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Resistencia al Fuego
                Surface(
                    color = Color(0xFFC62828).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fireplace,
                            contentDescription = null,
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = ficha.resistenciaFuego,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFC62828),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Aislación Térmica (U)
                Surface(
                    color = Color(0xFF2E7D32).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = ficha.aislacionTermica.takeWhile { it != '(' }.trim(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF2E7D32),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                // Aislamiento Acústico (STC)
                Surface(
                    color = Color(0xFF1565C0).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = ficha.aislacionAcustica,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF1565C0),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
