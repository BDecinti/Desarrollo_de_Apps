package com.example.myapplication.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.repository.FichaRepository

/**
 * Pantalla de Normativas Chilenas (NCh, OGUC) y Herramientas de Cálculo.
 * Organizada en 2 secciones mediante un selector segmentado.
 */
@Composable
fun NormativaScreen() {
    var selectedSection by remember { mutableStateOf(0) } // 0: Normativas, 1: Herramientas
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. CABECERA PRINCIPAL ---
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = "Normativa & Herramientas",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Regulaciones oficiales NCh/OGUC para diseño estructural, fuego, aislamiento térmico y acústico en Chile.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // --- 2. SELECTOR DE SECCIÓN (NORMATIVAS VS HERRAMIENTAS) ---
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = selectedSection == 0,
                onClick = { selectedSection = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) {
                Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Normativa NCh / OGUC")
            }

            SegmentedButton(
                selected = selectedSection == 1,
                onClick = { selectedSection = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) {
                Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Cálculo & Herramientas")
            }
        }

        // --- 3. CONTENIDO DE LA SECCIÓN SELECCIONADA ---
        if (selectedSection == 0) {
            // LISTA DE NORMATIVAS
            FichaRepository.normativas.forEach { norm ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = norm.codigo,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }

                            AssistChip(
                                onClick = { },
                                label = { Text(norm.categoria) }
                            )
                        }

                        Text(
                            text = norm.titulo,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = norm.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Consultando norma ${norm.codigo}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Consultar Resumen de Norma (PDF)")
                        }
                    }
                }
            }
        } else {
            // LISTA DE HERRAMIENTAS DE CÁLCULO
            FichaRepository.herramientas.forEach { herr ->
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = herr.tipo,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }

                        Text(
                            text = herr.nombre,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Text(
                            text = herr.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Button(
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "Iniciando: ${herr.nombre}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Build, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Abrir Herramienta de Cálculo")
                        }
                    }
                }
            }
        }
    }
}
