package com.example.myapplication.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.models.CategoriaItem
import com.example.myapplication.data.models.FichaTecnica
import com.example.myapplication.data.models.SugerenciaPersonalizada
import com.example.myapplication.data.models.Usuario
import com.example.myapplication.data.repository.FichaRepository
import com.example.myapplication.ui.components.FichaCard
import com.example.myapplication.ui.components.ProfileAvatar

/**
 * Pantalla Principal (Inicio).
 * Organizada en bloques visuales claros para facilitar el descubrimiento intuitivo de contenidos.
 */
@Composable
fun HomeScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateToCatalog: () -> Unit,
    onCategoriaClick: (String) -> Unit,
    featuredFichas: List<FichaTecnica>,
    onFichaClick: (FichaTecnica) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    usuarioActual: Usuario?,
    profilePhoto: Bitmap?,
    onTakePhoto: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavigateToConstrucciones: () -> Unit
) {
    var showLogoutConfirm by remember { mutableStateOf(false) }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
            title = { Text("Cerrar sesión") },
            text = { Text("¿Quieres cerrar la sesión de ${usuarioActual?.username.orEmpty()}?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                    onLogoutClick()
                }) { Text("Cerrar sesión") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirm = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        // --- 1. BANNER ENCABEZADO (HERO HEADER) ---
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // El encabezado se dibuja detrás de la barra de estado, pero su contenido queda por debajo
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(20.dp)
            ) {
                // Fila de Marca y Estado de Usuario
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Forest,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "Diseña Madera",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                            Text(
                                text = "CIM UC • Centro de Innovación en Madera",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                                )
                            )
                        }
                    }

                    // Botón de Perfil o Iniciar Sesión
                    if (usuarioActual != null) {
                        FilterChip(
                            selected = true,
                            onClick = { showLogoutConfirm = true },
                            label = { Text(usuarioActual.username, fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Person, null, modifier = Modifier.size(14.dp)) },
                            trailingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(14.dp)) }
                        )
                    } else {
                        FilledTonalButton(
                            onClick = onLoginClick,
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Login, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Ingresar", fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Explora soluciones constructivas y guías técnicas en madera certificadas para Chile",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Búsqueda Rápida en el Banner
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        onSearchQueryChange(it)
                        if (it.isNotBlank()) onNavigateToCatalog()
                    },
                    placeholder = { Text("Buscar sistema, material o código (ej: CLT, Pino...)", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 2. SECCIÓN DE PERSONALIZACIÓN Y PERFIL ---
        if (usuarioActual != null) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // Título de Sección
                Text(
                    text = "Tu Perfil & Recomendaciones",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Header de Usuario
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Recurso nativo (cámara): al tocar el avatar se puede actualizar la foto de perfil
                            ProfileAvatar(
                                initials = usuarioActual.avatarIniciales,
                                photo = profilePhoto,
                                size = 56.dp,
                                onCameraClick = onTakePhoto
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = usuarioActual.nombreCompleto,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = usuarioActual.perfilOcupacion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            TextButton(onClick = onLoginClick) {
                                Text("Cambiar", fontSize = 12.sp)
                            }
                        }

                        // Historial de Búsquedas
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.History,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Búsquedas recientes:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                items(usuarioActual.historialBusquedas) { query ->
                                    SuggestionChip(
                                        onClick = {
                                            onSearchQueryChange(query)
                                            onNavigateToCatalog()
                                        },
                                        label = { Text(query, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }

                        HorizontalDivider()

                        // Tarjetas de Sugerencias
                        usuarioActual.sugerenciasPersonalizadas.forEach { sug ->
                            SugerenciaCard(
                                sugerencia = sug,
                                onActionClick = {
                                    if (sug.tipo.contains("Mueble")) {
                                        onNavigateToConstrucciones()
                                    } else {
                                        onNavigateToCatalog()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        } else {
            // Invitación a Iniciar Sesión
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Sugerencias Personalizadas",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Inicia sesión o crea una cuenta para recibir recomendaciones según tu profesión.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = onLoginClick,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Ingresar", fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. CATEGORÍAS PRINCIPALES ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Categorías Principales",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                TextButton(onClick = onNavigateToCatalog) {
                    Text("Ver todas")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(FichaRepository.categorias) { cat ->
                    CategoriaCard(
                        categoria = cat,
                        onClick = {
                            onCategoriaClick(cat.nombre)
                            onNavigateToCatalog()
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- 4. SOLUCIONES DESTACADAS ---
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFFF57C00),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Soluciones Destacadas",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                featuredFichas.take(3).forEach { ficha ->
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

/**
 * Tarjeta para mostrar una sugerencia personalizada.
 */
@Composable
fun SugerenciaCard(
    sugerencia: SugerenciaPersonalizada,
    onActionClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
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
                AssistChip(
                    onClick = { },
                    label = { Text(sugerencia.badgeTexto, fontSize = 10.sp) },
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = sugerencia.tipo,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Text(
                text = sugerencia.titulo,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = sugerencia.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = sugerencia.motivo,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onActionClick) {
                    Text("Ver Recomendación", fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * Tarjeta para representar cada categoría en el carrusel horizontal.
 */
@Composable
fun CategoriaCard(
    categoria: CategoriaItem,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = categoria.nombre.take(3).uppercase(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = categoria.nombre,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${categoria.cantidadFichas} soluciones",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
