package com.example.myapplication.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.File

/**
 * Guarda la foto de perfil de cada usuario en el almacenamiento interno de la app
 * (no es accesible por otras aplicaciones).
 */
class ProfilePhotoStorage(context: Context) {

    private val dir = File(context.filesDir, "profile_photos").apply { mkdirs() }

    private fun fileFor(username: String): File {
        val safeName = username.lowercase().replace(Regex("[^a-z0-9_.-]"), "_")
        return File(dir, "$safeName.jpg")
    }

    fun load(username: String): Bitmap? {
        val file = fileFor(username)
        return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null
    }

    /**
     * Toma la foto capturada por la cámara, la reduce, corrige su rotación,
     * la recorta en formato cuadrado y la guarda. Devuelve el bitmap resultante.
     */
    fun saveFromCapture(username: String, captured: File): Bitmap? {
        if (!captured.exists()) return null

        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(captured.absolutePath, bounds)
        if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

        var sample = 1
        while (bounds.outWidth / (sample * 2) >= TARGET_SIZE && bounds.outHeight / (sample * 2) >= TARGET_SIZE) {
            sample *= 2
        }
        val decoded = BitmapFactory.decodeFile(
            captured.absolutePath,
            BitmapFactory.Options().apply { inSampleSize = sample }
        ) ?: return null

        val rotated = rotateIfNeeded(decoded, captured)
        val side = minOf(rotated.width, rotated.height)
        val square = Bitmap.createBitmap(
            rotated,
            (rotated.width - side) / 2,
            (rotated.height - side) / 2,
            side,
            side
        )
        val finalBitmap = if (side > TARGET_SIZE) {
            Bitmap.createScaledBitmap(square, TARGET_SIZE, TARGET_SIZE, true)
        } else {
            square
        }

        fileFor(username).outputStream().use { out ->
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        captured.delete() // no dejamos copias temporales en caché
        return finalBitmap
    }

    private fun rotateIfNeeded(bitmap: Bitmap, source: File): Bitmap {
        val orientation = try {
            ExifInterface(source.absolutePath)
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        } catch (_: Exception) {
            ExifInterface.ORIENTATION_NORMAL
        }
        val degrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> return bitmap
        }
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private companion object {
        const val TARGET_SIZE = 512
    }
}
