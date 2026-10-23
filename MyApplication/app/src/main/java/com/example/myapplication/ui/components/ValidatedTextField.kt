package com.example.myapplication.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/** Verde de "campo correcto", legible en tema claro y oscuro. */
private val SuccessGreen = Color(0xFF2E7D32)

/**
 * Campo de texto con retroalimentación visual completa:
 *  - borde y texto de apoyo en rojo + ícono de error cuando [error] no es null,
 *  - ícono de éxito cuando [isValid] es true,
 *  - alternador de visibilidad para contraseñas.
 *
 * Es solo presentación: no contiene reglas de validación (esas viven en la capa de dominio).
 */
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    error: String?,
    isValid: Boolean,
    modifier: Modifier = Modifier,
    helperText: String? = null,
    placeholder: String? = null,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    onBlur: () -> Unit = {}
) {
    var hadFocus by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it) } },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = if (error != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingIcon = {
            when {
                isPassword -> IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
                else -> Crossfade(
                    targetState = when {
                        error != null -> 1
                        isValid -> 2
                        else -> 0
                    },
                    label = "FieldStatusIcon"
                ) { status ->
                    when (status) {
                        1 -> Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = "Campo con error",
                            tint = MaterialTheme.colorScheme.error
                        )
                        2 -> Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Campo correcto",
                            tint = SuccessGreen
                        )
                        else -> Unit
                    }
                }
            }
        },
        isError = error != null,
        supportingText = {
            // Texto animado: el espacio siempre está reservado para que el formulario no "salte".
            AnimatedContent(
                targetState = error ?: helperText.orEmpty(),
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "FieldSupportingText"
            ) { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.onFocusChanged { focus ->
            if (hadFocus && !focus.isFocused) onBlur()
            hadFocus = focus.isFocused
        }
    )
}
