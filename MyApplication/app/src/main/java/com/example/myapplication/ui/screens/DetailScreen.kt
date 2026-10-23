package com.example.myapplication.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.ui.components.FavoriteButton

/**
 * Pantalla de Detalle Técnico de la Ficha Constructiva.
 * Muestra especificaciones clave (Fuego, Térmico, Acústico) y permite compartir mediante Intent nativo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    ficha: FichaTecnica,
    onBackClick: () -> Unit,
    onFavoriteToggle: (String) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = ficha.codigo,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = ficha.categoria,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // Recurso Nativo 1: Intent Nativo de Compartir (ACTION_SEND)
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "📌 *Ficha Técnica Diseña Madera - CIM UC*\n" +
                                        "Código: ${ficha.codigo}\n" +
                                        "Título: ${ficha.titulo}\n" +
                                        "Sistema: ${ficha.sistemaConstructivo} | Material: ${ficha.material}\n" +
                                        "Resistencia Fuego: ${ficha.resistenciaFuego}\n" +
                                        "Aislación Térmica: ${ficha.aislacionTermica}\n" +
                                        "Aislación Acústica: ${ficha.aislacionAcustica}\n\n" +
                                        "Consultado desde la App Móvil Diseña Madera (CIM UC)."
                            )
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Compartir Ficha Técnica ${ficha.codigo}")
                        context.startActivity(shareIntent)
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Compartir")
                    }

                    // Recurso Nativo 2: Feedback Háptico al marcar Favorito
                    FavoriteButton(
                        isFavorite = ficha.esFavorito,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onFavoriteToggle(ficha.id)
                        },
                        inactiveTint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 1. BANNER ENCABEZADO DE LA FICHA ---
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = ficha.titulo,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text(ficha.sistemaConstructivo) }
                        )
                        AssistChip(
                            onClick = { },
                            label = { Text(ficha.material) }
                        )
                    }
                }
            }

            // --- 2. RESUMEN TÉCNICO EN TRES TARJETAS (FUEGO, TÉRMICO, ACÚSTICO) ---
            Text(
                text = "Desempeño Técnico Principal",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tarjeta Fuego
                Surface(
                    color = Color(0xFFFFEBEE),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fireplace,
                            contentDescription = null,
                            tint = Color(0xFFC62828),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Fuego",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFC62828))
                        )
                        Text(
                            text = ficha.resistenciaFuego,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB71C1C)
                            )
                        )
                    }
                }

                // Tarjeta Térmico
                Surface(
                    color = Color(0xFFE8F5E9),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Thermostat,
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Térmico",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2E7D32))
                        )
                        Text(
                            text = ficha.aislacionTermica.takeWhile { it != '(' }.trim(),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                        )
                    }
                }

                // Tarjeta Acústico
                Surface(
                    color = Color(0xFFE3F2FD),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = Color(0xFF1565C0),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Acústico",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF1565C0))
                        )
                        Text(
                            text = ficha.aislacionAcustica,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D47A1)
                            )
                        )
                    }
                }
            }

            // --- 3. DESCRIPCIÓN GENERAL ---
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Descripción de la Solución",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = ficha.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Ubicación / Aplicación: ${ficha.aplicacion}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // --- 4. DIMENSIONES Y ESCUADRÍAS ---
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Dimensiones y Escuadría Estándar:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ficha.dimensionesEstandard,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // --- 5. NORMATIVA RELACIONADA ---
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Normativa & Regulaciones NCh",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ficha.normativaRelacionada.forEach { norma ->
                        Text(
                            text = "• $norma",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }

            // --- 6. ACCIONES NATIVAS ---
            OutlinedButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    val webIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://madera.uc.cl")
                    )
                    try {
                        context.startActivity(webIntent)
                    } catch (_: Exception) {
                        Toast.makeText(context, "No se pudo abrir el navegador", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ver Portal Oficial CIM UC (Navegador Nativo)")
            }

            Button(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    Toast.makeText(
                        context,
                        "Descargando ${ficha.documentoPdfNombre} en almacenamiento local",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Descargar Ficha Técnica PDF (${ficha.codigo})")
            }
        }
    }
}
