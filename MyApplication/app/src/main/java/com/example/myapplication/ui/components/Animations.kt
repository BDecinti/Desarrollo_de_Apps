package com.example.myapplication.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

/**
 * Sacude horizontalmente el componente cada vez que [trigger] aumenta.
 * Se usa para dar retroalimentación cuando un formulario se envía con errores.
 */
fun Modifier.shake(trigger: Int): Modifier = composed {
    val offsetX = remember { Animatable(0f) }
    LaunchedEffect(trigger) {
        if (trigger > 0) {
            for (target in listOf(-14f, 14f, -10f, 10f, -5f, 0f)) {
                offsetX.animateTo(target, tween(durationMillis = 45))
            }
        }
    }
    graphicsLayer { translationX = offsetX.value }
}
