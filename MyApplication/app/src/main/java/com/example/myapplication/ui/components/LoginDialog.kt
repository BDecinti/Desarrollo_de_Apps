package com.example.myapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.data.models.Usuario
import com.example.myapplication.data.repository.FichaRepository
import com.example.myapplication.domain.validation.FormValidators
import com.example.myapplication.ui.viewmodel.AuthField
import com.example.myapplication.ui.viewmodel.AuthFormState
import com.example.myapplication.ui.viewmodel.AuthMode

/**
 * Diálogo de acceso (iniciar sesión / crear cuenta).
 *
 * Es un componente de presentación: recibe el [state] ya validado desde el ViewModel
 * y notifica las acciones del usuario mediante callbacks.
 */
@Composable
fun LoginDialog(
    state: AuthFormState,
    serverError: String?,
    onModeChange: (AuthMode) -> Unit,
    onFieldChange: (AuthField, String) -> Unit,
    onFieldBlur: (AuthField) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,
    onQuickUserSelect: (Usuario) -> Unit,
    onDismiss: () -> Unit
) {
    val isLogin = state.mode == AuthMode.LOGIN

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
                    .shake(state.shakeCount),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // --- Encabezado ---
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Column {
                        Text(
                            text = if (isLogin) "Iniciar sesión" else "Crear cuenta",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Diseña Madera · CIM UC",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                TabRow(
                    selectedTabIndex = if (isLogin) 0 else 1,
                    containerColor = Color.Transparent
                ) {
                    Tab(
                        selected = isLogin,
                        onClick = { onModeChange(AuthMode.LOGIN) },
                        text = { Text("Iniciar sesión") }
                    )
                    Tab(
                        selected = !isLogin,
                        onClick = { onModeChange(AuthMode.REGISTER) },
                        text = { Text("Crear cuenta") }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // --- Usuario ---
                ValidatedTextField(
                    value = state.username,
                    onValueChange = { onFieldChange(AuthField.USERNAME, it) },
                    label = if (isLogin) "Usuario" else "Usuario*",
                    leadingIcon = Icons.Default.Person,
                    error = state.errorFor(AuthField.USERNAME),
                    isValid = !isLogin && state.isFieldValid(AuthField.USERNAME),
                    helperText = if (isLogin) null else "4 a 20 caracteres: letras, números, punto o guion bajo",
                    onBlur = { onFieldBlur(AuthField.USERNAME) },
                    modifier = Modifier.fillMaxWidth()
                )

                // --- Contraseña ---
                ValidatedTextField(
                    value = state.password,
                    onValueChange = { onFieldChange(AuthField.PASSWORD, it) },
                    label = if (isLogin) "Contraseña" else "Contraseña*",
                    leadingIcon = Icons.Default.Lock,
                    error = state.errorFor(AuthField.PASSWORD),
                    isValid = state.isFieldValid(AuthField.PASSWORD),
                    isPassword = true,
                    passwordVisible = state.passwordVisible,
                    onTogglePasswordVisibility = onTogglePasswordVisibility,
                    keyboardType = KeyboardType.Password,
                    imeAction = if (isLogin) ImeAction.Done else ImeAction.Next,
                    onImeAction = { if (isLogin) onSubmit() },
                    onBlur = { onFieldBlur(AuthField.PASSWORD) },
                    modifier = Modifier.fillMaxWidth()
                )

                if (isLogin) {
                    ServerErrorBanner(message = serverError)

                    Button(
                        onClick = onSubmit,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text("Ingresar")
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Cuentas de demostración",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Toca una para entrar sin escribir tus datos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FichaRepository.usuariosDemo.forEach { demoUser ->
                            DemoUserRow(user = demoUser, onClick = { onQuickUserSelect(demoUser) })
                        }
                    }
                } else {
                    // Indicador animado de fortaleza de contraseña
                    PasswordStrengthIndicator(password = state.password)

                    ValidatedTextField(
                        value = state.confirmPassword,
                        onValueChange = { onFieldChange(AuthField.CONFIRM_PASSWORD, it) },
                        label = "Confirmar contraseña*",
                        leadingIcon = Icons.Default.LockReset,
                        error = state.errorFor(AuthField.CONFIRM_PASSWORD),
                        isValid = state.isFieldValid(AuthField.CONFIRM_PASSWORD),
                        isPassword = true,
                        passwordVisible = state.passwordVisible,
                        onTogglePasswordVisibility = onTogglePasswordVisibility,
                        keyboardType = KeyboardType.Password,
                        onBlur = { onFieldBlur(AuthField.CONFIRM_PASSWORD) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ValidatedTextField(
                        value = state.fullName,
                        onValueChange = { onFieldChange(AuthField.FULL_NAME, it) },
                        label = "Nombre completo*",
                        leadingIcon = Icons.Default.Badge,
                        error = state.errorFor(AuthField.FULL_NAME),
                        isValid = state.isFieldValid(AuthField.FULL_NAME),
                        onBlur = { onFieldBlur(AuthField.FULL_NAME) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ValidatedTextField(
                        value = state.occupation,
                        onValueChange = { onFieldChange(AuthField.OCCUPATION, it) },
                        label = "Ocupación (opcional)",
                        placeholder = "Ej: Carpintero, Arquitecto",
                        leadingIcon = Icons.Default.Work,
                        error = state.errorFor(AuthField.OCCUPATION),
                        isValid = false,
                        onBlur = { onFieldBlur(AuthField.OCCUPATION) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ValidatedTextField(
                        value = state.interest,
                        onValueChange = { onFieldChange(AuthField.INTEREST, it) },
                        label = "Tema de interés (opcional)",
                        placeholder = "Ej: Cómodas, Pérgolas, CLT",
                        leadingIcon = Icons.Default.AutoAwesome,
                        error = state.errorFor(AuthField.INTEREST),
                        isValid = false,
                        imeAction = ImeAction.Done,
                        onImeAction = onSubmit,
                        onBlur = { onFieldBlur(AuthField.INTEREST) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ServerErrorBanner(message = serverError)

                    Button(
                        onClick = onSubmit,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text("Crear cuenta e ingresar")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                }
            }
        }
    }
}

/** Mensaje de error que viene de la lógica de autenticación (credenciales incorrectas, etc.). */
@Composable
private fun ServerErrorBanner(message: String?) {
    AnimatedVisibility(
        visible = message != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = message.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

/** Barra animada que indica qué tan segura es la contraseña escrita. */
@Composable
private fun PasswordStrengthIndicator(password: String) {
    val score = FormValidators.passwordStrength(password)
    val progress by animateFloatAsState(targetValue = score / 4f, label = "StrengthProgress")
    val color by animateColorAsState(
        targetValue = when {
            score <= 1 -> MaterialTheme.colorScheme.error
            score == 2 -> Color(0xFFF57C00)
            else -> Color(0xFF2E7D32)
        },
        label = "StrengthColor"
    )

    AnimatedVisibility(visible = password.isNotEmpty(), enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            LinearProgressIndicator(
                progress = { progress },
                color = color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Seguridad: ${FormValidators.passwordStrengthLabel(score)}",
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}

@Composable
private fun DemoUserRow(user: Usuario, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.avatarIniciales,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${user.nombreCompleto} (${user.username})",
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    text = user.perfilOcupacion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
