package com.example.myapplication.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val FavoriteRed = Color(0xFFD32F2F)

/**
 * Botón de favorito con retroalimentación animada: el corazón "rebota" y cambia de color suavemente
 * al activarse. No anima en la primera composición para no distraer al hacer scroll en listas.
 */
@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonSize: Dp = 40.dp,
    inactiveTint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val scale = remember { Animatable(1f) }
    var isFirstComposition by remember { mutableStateOf(true) }

    LaunchedEffect(isFavorite) {
        if (isFirstComposition) {
            isFirstComposition = false
        } else if (isFavorite) {
            scale.snapTo(0.6f)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        } else {
            scale.snapTo(0.85f)
            scale.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
        }
    }

    val tint by animateColorAsState(
        targetValue = if (isFavorite) FavoriteRed else inactiveTint,
        label = "FavoriteTint"
    )

    IconButton(onClick = onClick, modifier = modifier.size(buttonSize)) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) "Quitar de favoritos" else "Guardar en favoritos",
            tint = tint,
            modifier = Modifier.graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
        )
    }
}
