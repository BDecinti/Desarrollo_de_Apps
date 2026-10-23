package com.example.myapplication.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Avatar de usuario: muestra la foto de perfil (si existe) o las iniciales.
 * Si se entrega [onCameraClick] aparece una insignia de cámara para cambiar la foto.
 */
@Composable
fun ProfileAvatar(
    initials: String,
    photo: Bitmap?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    onCameraClick: (() -> Unit)? = null
) {
    val clickModifier = if (onCameraClick != null) Modifier.clickable { onCameraClick() } else Modifier

    Box(modifier = modifier.size(size)) {
        Crossfade(
            targetState = photo,
            label = "AvatarCrossfade",
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .then(clickModifier)
        ) { currentPhoto ->
            if (currentPhoto != null) {
                val imageBitmap = remember(currentPhoto) { currentPhoto.asImageBitmap() }
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                )
            } else {
                Surface(color = MaterialTheme.colorScheme.primary, shape = CircleShape, modifier = Modifier.size(size)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = initials,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        if (onCameraClick != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(size * 0.42f)
                    .background(MaterialTheme.colorScheme.tertiary, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .clickable { onCameraClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = "Cambiar foto de perfil",
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.size(size * 0.24f)
                )
            }
        }
    }
}
