package com.example.myapplication.device

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Diálogo que explica por qué se necesita un permiso cuando el usuario lo rechazó
 * y ofrece abrir los ajustes de la app para concederlo.
 */
@Composable
fun PermissionDeniedDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Lock, contentDescription = null) },
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                context.startActivity(intent)
            }) { Text("Abrir ajustes") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Ahora no") }
        }
    )
}
