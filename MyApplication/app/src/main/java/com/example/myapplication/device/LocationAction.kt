package com.example.myapplication.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat

/**
 * Obtiene una única lectura de ubicación (sin seguimiento continuo) para proteger la privacidad y la batería.
 */
object LocationFetcher {

    fun isLocationEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return false
        return LocationManagerCompat.isLocationEnabled(manager)
    }

    @SuppressLint("MissingPermission") // El permiso se verifica en rememberLocationAction antes de llamar aquí.
    fun getCurrent(context: Context, onResult: (Location?) -> Unit) {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (manager == null) {
            onResult(null)
            return
        }
        try {
            val provider = when {
                manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                manager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                else -> null
            }
            if (provider == null) {
                onResult(null)
                return
            }
            LocationManagerCompat.getCurrentLocation(
                manager,
                provider,
                null,
                ContextCompat.getMainExecutor(context)
            ) { location -> onResult(location) }
        } catch (_: SecurityException) {
            onResult(null)
        }
    }
}

/**
 * Recurso nativo: UBICACIÓN.
 *
 * Solicita el permiso en tiempo de ejecución (aproximado o preciso), valida que el GPS esté activo
 * y entrega las coordenadas. Si el usuario rechaza el permiso se le explica cómo activarlo.
 */
@Composable
fun rememberLocationAction(
    onLoadingChange: (Boolean) -> Unit,
    onLocation: (latitude: Double, longitude: Double) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val context = LocalContext.current
    var showDenied by remember { mutableStateOf(false) }

    val fetch: () -> Unit = {
        if (!LocationFetcher.isLocationEnabled(context)) {
            onError("Activa la ubicación del dispositivo para detectar tu zona")
        } else {
            onLoadingChange(true)
            LocationFetcher.getCurrent(context) { location ->
                onLoadingChange(false)
                if (location == null) {
                    onError("No pudimos obtener tu ubicación. Inténtalo nuevamente.")
                } else {
                    onLocation(location.latitude, location.longitude)
                }
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.any { it }) fetch() else showDenied = true
    }

    if (showDenied) {
        PermissionDeniedDialog(
            title = "Permiso de ubicación",
            message = "Usamos tu ubicación solo una vez para recomendarte maderas según el clima de tu zona. " +
                    "Puedes activarla desde los ajustes de la aplicación.",
            onDismiss = { showDenied = false }
        )
    }

    return {
        val granted = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).any { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }

        if (granted) {
            fetch()
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        }
    }
}
