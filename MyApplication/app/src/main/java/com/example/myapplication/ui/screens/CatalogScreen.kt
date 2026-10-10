package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.ui.components.FichaCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategoria: String?,
    selectedSistema: String,
    selectedMaterial: String,
    selectedAplicacion: String,
    onCategoriaRemove: () -> Unit,
    onSistemaRemove: () -> Unit,
    onMaterialRemove: () -> Unit,
    onAplicacionRemove: () -> Unit,
    onOpenFilterDialog: () -> Unit,
    fichas: List<FichaTecnica>,
    onFichaClick: (FichaTecnica) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onResetFilters: () -> Unit
) {
    val activeFiltersCount = listOfNotNull(
        selectedCategoria,
        if (selectedSistema != "Todos") selectedSistema else null,
        if (selectedMaterial != "Todos") selectedMaterial else null,
        if (selectedAplicacion != "Todas") selectedAplicacion else null
    ).size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Search & Filter Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Buscar en catálogo...", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Filter Button
            BadgedBox(
                badge = {
                    if (activeFiltersCount > 0) {
                        Badge { Text(activeFiltersCount.toString()) }
                    }
                }
            ) {
                Button(
                    onClick = onOpenFilterDialog,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp)
                ) {
                    Icon(imageVector = Icons.Default.FilterList, contentDescription = "Filtros")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Filtros")
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Active Filter Chips Row
        if (activeFiltersCount > 0 || searchQuery.isNotBlank()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (searchQuery.isNotBlank()) {
                    item {
                        InputChip(
                            selected = true,
                            onClick = { onSearchQueryChange("") },
                            label = { Text("Texto: \"$searchQuery\"") },
                            trailingIcon = {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }

                if (selectedCategoria != null) {
                    item {
                        InputChip(
                            selected = true,
                            onClick = onCategoriaRemove,
                            label = { Text("Cat: $selectedCategoria") },
                            trailingIcon = {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }

                if (selectedSistema != "Todos") {
                    item {
                        InputChip(
                            selected = true,
                            onClick = onSistemaRemove,
                            label = { Text("Sistema: $selectedSistema") },
                            trailingIcon = {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }

                if (selectedMaterial != "Todos") {
                    item {
                        InputChip(
                            selected = true,
                            onClick = onMaterialRemove,
                            label = { Text("Mat: $selectedMaterial") },
                            trailingIcon = {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }

                if (selectedAplicacion != "Todas") {
                    item {
                        InputChip(
                            selected = true,
                            onClick = onAplicacionRemove,
                            label = { Text("Uso: $selectedAplicacion") },
                            trailingIcon = {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        )
                    }
                }

                item {
                    TextButton(onClick = onResetFilters) {
                        Text("Limpiar todo", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Count header
        Text(
            text = "${fichas.size} soluciones encontradas",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Fichas List
        if (fichas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No se encontraron fichas técnicas",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Intenta modificar los filtros de búsqueda o el texto.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onResetFilters) {
                        Text("Restablecer Filtros")
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(fichas, key = { it.id }) { ficha ->
                    FichaCard(
                        ficha = ficha,
                        onClick = { onFichaClick(ficha) },
                        onFavoriteToggle = { onFavoriteToggle(ficha.id) }
                    )
                }
            }
        }
    }
}
