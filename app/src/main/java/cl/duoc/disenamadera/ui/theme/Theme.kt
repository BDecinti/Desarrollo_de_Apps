package cl.duoc.disenamadera.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = MaderaPrimary,
    onPrimary = MaderaOnPrimary,
    primaryContainer = MaderaPrimaryContainer,
    onPrimaryContainer = MaderaOnPrimaryContainer,
    secondary = BosqueSecondary,
    onSecondary = BosqueOnSecondary,
    secondaryContainer = BosqueSecondaryContainer,
    onSecondaryContainer = BosqueOnSecondaryContainer,
    background = CremaBackground,
    onBackground = TextoOscuro,
    surface = CremaBackground,
    onSurface = TextoOscuro,
    surfaceVariant = CremaSurfaceVariant,
    error = ErrorRojo
)

private val DarkColors = darkColorScheme(
    primary = MaderaPrimaryDark,
    primaryContainer = MaderaPrimaryContainerDark,
    secondary = BosqueSecondaryDark,
    secondaryContainer = BosqueSecondaryContainerDark,
    background = SuperficieOscura,
    surface = SuperficieOscura
)

@Composable
fun DisenaMaderaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
