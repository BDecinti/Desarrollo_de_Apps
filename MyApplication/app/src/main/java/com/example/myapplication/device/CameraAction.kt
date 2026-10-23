package com.example.myapplication.device

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * Recurso nativo: CÁMARA.
 *
 * Devuelve una acción que:
 *  1. Verifica que el dispositivo tenga cámara.
 *  2. Solicita el permiso CAMERA en tiempo de ejecución (y explica cómo activarlo si se rechaza).
 *  3. Abre la cámara del sistema guardando la foto mediante un FileProvider (sin exponer rutas de archivo).
 */
@Composable
fun rememberCameraAction(
    onPhotoCaptured: (File) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    var pendingPath by rememberSaveable { mutableStateOf<String?>(null) }
    var showDenied by remember { mutableStateOf(false) }

    val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val path = pendingPath
        pendingPath = null
        if (success && path != null) onPhotoCaptured(File(path))
    }

    val launchCamera: () -> Unit = {
        try {
            val dir = File(context.cacheDir, "camera").apply { mkdirs() }
            val file = File(dir, "profile_capture.jpg")
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            pendingPath = file.absolutePath
            takePicture.launch(uri)
        } catch (_: Exception) {
            onError("No se pudo abrir la cámara en este dispositivo")
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) launchCamera() else showDenied = true
    }

    if (showDenied) {
        PermissionDeniedDialog(
            title = "Permiso de cámara",
            message = "Para actualizar tu foto de perfil necesitamos acceder a la cámara. " +
                    "Puedes activarlo desde los ajustes de la aplicación.",
            onDismiss = { showDenied = false }
        )
    }

    return {
        val hasCamera = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        when {
            !hasCamera -> onError("Este dispositivo no tiene cámara disponible")
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED -> launchCamera()
            else -> permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}
