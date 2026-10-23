package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.ui.components.FichaCard

/**
 * Pantalla de Favoritos.
 * Muestra las fichas técnicas guardadas por el usuario, persistidas en almacenamiento local.
 */
@Composable
fun FavoritesScreen(
    favoriteFichas: List<FichaTecnica>,
    onFichaClick: (FichaTecnica) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    onExploreClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // --- CABECERA SECCIÓN FAVORITOS ---
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(24.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Fichas Guardadas",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Accede a tus soluciones guardadas en almacenamiento local.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = Color(0xFFD32F2F),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${favoriteFichas.size}",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CONTENIDO ---
        if (favoriteFichas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Aún no tienes fichas guardadas",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Guarda soluciones constructivas para consultarlas rápidamente desde cualquier lugar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onExploreClick) {
                        Text("Explorar Catálogo")
                    }
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(favoriteFichas, key = { it.id }) { ficha ->
                    FichaCard(
                        ficha = ficha,
                        onClick = { onFichaClick(ficha) },
                        onFavoriteToggle = { onFavoriteToggle(ficha.id) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}
