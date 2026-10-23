package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Carpenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.models.ConstruccionBasica
import com.example.myapplication.data.models.RecomendacionMadera
import com.example.myapplication.data.repository.FichaRepository
import com.example.myapplication.domain.climate.ClimateZone
import com.example.myapplication.ui.components.ClimateZoneCard

/**
 * Pantalla de Guía de Muebles y Construcciones Básicas.
 * Permite filtrar muebles y proyectos por tipo, clima (calor/humedad) y utilidad de la madera.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstruccionesBasicasScreen(
    climateZone: ClimateZone?,
    onLocationDetected: (latitude: Double, longitude: Double) -> Boolean
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas") }
    var selectedClimaFilter by remember { mutableStateOf("Todos") }
    var selectedUtilidadFilter by remember { mutableStateOf("Todas") }

    val categories = remember {
        listOf("Todas", "Almacenamiento", "Muebles de Interior", "Muebles de Exterior")
    }

    val climas = remember {
        listOf("Todos", "Cálido y Seco", "Húmedo y Exterior", "Interior y Templado")
    }

    val utilidades = remember {
        listOf("Todas", "Estética y Elegancia", "Funcional y Económico", "Alta Resistencia")
    }

    val filteredConstrucciones = remember(
        searchQuery,
        selectedCategory,
        selectedClimaFilter,
        selectedUtilidadFilter
    ) {
        FichaRepository.construccionesBasicas.filter { item ->
            val matchesCategory = selectedCategory == "Todas" || item.categoria.equals(selectedCategory, ignoreCase = true)

            val matchesQuery = if (searchQuery.isBlank()) true else {
                val q = searchQuery.trim().lowercase()
                item.titulo.lowercase().contains(q) ||
                        item.descripcion.lowercase().contains(q) ||
                        item.categoria.lowercase().contains(q) ||
                        item.recomendacionesMadera.any { rec ->
                            rec.nombreMadera.lowercase().contains(q) ||
                                    rec.porqueRecomendada.lowercase().contains(q)
                        }
            }

            val hasMatchingWood = item.recomendacionesMadera.any { rec ->
                val matchesClima = selectedClimaFilter == "Todos" ||
                        rec.categoriaClima.equals(selectedClimaFilter, ignoreCase = true)
                val matchesUtilidad = selectedUtilidadFilter == "Todas" ||
                        rec.categoriaUtilidad.equals(selectedUtilidadFilter, ignoreCase = true)
                matchesClima && matchesUtilidad
            }

            matchesCategory && matchesQuery && hasMatchingWood
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. BANNER PRINCIPAL ---
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
                        imageVector = Icons.Default.Carpenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(26.dp)
                    )
                    Text(
                        text = "Guía de Muebles & Proyectos",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Aprende a fabricar cómodas, mesas, closets y pérgolas con la madera adecuada según el clima (sol/lluvia) y su resistencia.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // --- 1.b ZONA CLIMÁTICA (recurso nativo: ubicación) ---
        ClimateZoneCard(
            zone = climateZone,
            activeClimateFilter = selectedClimaFilter,
            onLocationDetected = onLocationDetected,
            onApplyZoneFilter = { selectedClimaFilter = it.label }
        )

        // --- 2. BARRA DE BÚSQUEDA ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar mueble, tipo de madera o clima...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        // --- 3. FILTROS DE CATEGORÍA ---
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "1. Categoría de Mueble:",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) }
                    )
                }
            }
        }

        // --- 4. FILTROS DE CLIMA Y UTILIDAD DE LA MADERA ---
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "2. Recomendación de Madera por Clima o Uso",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Filtro Clima
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Por Clima / Exposición:", style = MaterialTheme.typography.labelSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(climas) { clima ->
                            FilterChip(
                                selected = selectedClimaFilter == clima,
                                onClick = { selectedClimaFilter = clima },
                                label = { Text(clima, fontSize = 12.sp) },
                                leadingIcon = when (clima) {
                                    "Cálido y Seco" -> ({ Icon(Icons.Default.LightMode, null, modifier = Modifier.size(14.dp)) })
                                    "Húmedo y Exterior" -> ({ Icon(Icons.Default.WaterDrop, null, modifier = Modifier.size(14.dp)) })
                                    "Interior y Templado" -> ({ Icon(Icons.Default.Home, null, modifier = Modifier.size(14.dp)) })
                                    else -> null
                                }
                            )
                        }
                    }
                }

                // Filtro Utilidad
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Por Utilidad de la Madera:", style = MaterialTheme.typography.labelSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(utilidades) { utilidad ->
                            FilterChip(
                                selected = selectedUtilidadFilter == utilidad,
                                onClick = { selectedUtilidadFilter = utilidad },
                                label = { Text(utilidad, fontSize = 12.sp) },
                                leadingIcon = when (utilidad) {
                                    "Estética y Elegancia" -> ({ Icon(Icons.Default.Star, null, modifier = Modifier.size(14.dp)) })
                                    "Funcional y Económico" -> ({ Icon(Icons.Default.Build, null, modifier = Modifier.size(14.dp)) })
                                    "Alta Resistencia" -> ({ Icon(Icons.Default.Thermostat, null, modifier = Modifier.size(14.dp)) })
                                    else -> null
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- 5. LISTA DE PROYECTOS Y MUEBLES ---
        if (filteredConstrucciones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No se encontraron proyectos con los filtros seleccionados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            filteredConstrucciones.forEach { item ->
                ConstruccionCard(
                    construccion = item,
                    selectedClimaFilter = selectedClimaFilter,
                    selectedUtilidadFilter = selectedUtilidadFilter
                )
            }
        }
    }
}

/**
 * Tarjeta individual para cada tipo de mueble o proyecto.
 */
@Composable
fun ConstruccionCard(
    construccion: ConstruccionBasica,
    selectedClimaFilter: String,
    selectedUtilidadFilter: String
) {
    var showConsejos by remember { mutableStateOf(false) }

    val filteredMaderas = remember(construccion, selectedClimaFilter, selectedUtilidadFilter) {
        construccion.recomendacionesMadera.filter { rec ->
            val matchesClima = selectedClimaFilter == "Todos" ||
                    rec.categoriaClima.equals(selectedClimaFilter, ignoreCase = true)
            val matchesUtilidad = selectedUtilidadFilter == "Todas" ||
                    rec.categoriaUtilidad.equals(selectedUtilidadFilter, ignoreCase = true)
            matchesClima && matchesUtilidad
        }
    }

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssistChip(
                    onClick = { },
                    label = { Text(construccion.categoria) },
                    leadingIcon = { Icon(Icons.Default.Park, null, modifier = Modifier.size(16.dp)) }
                )

                val (diffBg, diffColor) = when (construccion.dificultadEjecucion) {
                    "Fácil" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
                    "Intermedio" -> Color(0xFFFFF3E0) to Color(0xFFE65100)
                    else -> Color(0xFFFFEBEE) to Color(0xFFC62828)
                }

                Surface(
                    color = diffBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Dificultad: ${construccion.dificultadEjecucion}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = diffColor
                        )
                    )
                }
            }

            Text(
                text = construccion.titulo,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = construccion.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Desplegable de Consejos Clave
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showConsejos = !showConsejos }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Consejos Clave de Armado (${construccion.consejosConstruccion.size})",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                    Icon(
                        imageVector = if (showConsejos) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            }

            AnimatedVisibility(
                visible = showConsejos,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    construccion.consejosConstruccion.forEach { consejo ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("•", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(
                                text = consejo,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // Subtítulo Maderas Recomendadas
            Text(
                text = "Maderas Recomendadas por Clima y Uso:",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredMaderas.forEach { rec ->
                    RecomendacionMaderaCard(rec)
                }
            }
        }
    }
}

/**
 * Tarjeta para cada madera recomendada dentro de un mueble.
 */
@Composable
fun RecomendacionMaderaCard(rec: RecomendacionMadera) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = rec.nombreMadera,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = when {
                                rec.categoriaClima.contains("Cálido", ignoreCase = true) -> Icons.Default.LightMode
                                rec.categoriaClima.contains("Húmedo", ignoreCase = true) -> Icons.Default.WaterDrop
                                else -> Icons.Default.Home
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = rec.categoriaClima,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            // Badge de Utilidad
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = when {
                            rec.categoriaUtilidad.contains("Estética", ignoreCase = true) -> Icons.Default.Star
                            rec.categoriaUtilidad.contains("Funcional", ignoreCase = true) -> Icons.Default.Build
                            else -> Icons.Default.Thermostat
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = rec.categoriaUtilidad,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }

            Text(
                text = rec.porqueRecomendada,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Chips de características
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                rec.caracteristicasClave.forEach { feat ->
                    SuggestionChip(
                        onClick = { },
                        label = { Text(feat, fontSize = 10.sp) },
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}
