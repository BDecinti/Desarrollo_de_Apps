package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.StoredUser
import com.example.myapplication.data.local.UserStorage
import com.example.myapplication.data.models.SugerenciaPersonalizada
import com.example.myapplication.data.models.Usuario
import com.example.myapplication.domain.security.PasswordHasher

/**
 * Punto único de acceso a los usuarios: cuentas de demostración + cuentas registradas
 * (persistidas localmente con contraseña hasheada).
 */
class AuthRepository(context: Context) {

    private val storage = UserStorage(context)

    fun findUser(username: String): Usuario? {
        val name = username.trim()
        FichaRepository.usuariosDemo.firstOrNull { it.username.equals(name, ignoreCase = true) }
            ?.let { return it }
        return storage.find(name)?.toUsuario()
    }

    fun isUsernameTaken(username: String): Boolean = findUser(username) != null

    /** Devuelve el usuario si las credenciales son correctas, o null en caso contrario. */
    fun authenticate(username: String, password: String): Usuario? {
        val name = username.trim()
        FichaRepository.usuariosDemo.firstOrNull {
            it.username.equals(name, ignoreCase = true) && it.password == password
        }?.let { return it }

        val stored = storage.find(name) ?: return null
        return if (PasswordHasher.verify(password, stored.passwordHash)) stored.toUsuario() else null
    }

    /** Registra una cuenta nueva. Devuelve null si el nombre de usuario ya existe. */
    fun register(
        username: String,
        password: String,
        nombre: String,
        perfil: String,
        interes: String
    ): Usuario? {
        if (isUsernameTaken(username)) return null
        val stored = StoredUser(
            id = "usr_${System.currentTimeMillis()}",
            username = username.trim(),
            passwordHash = PasswordHasher.hash(password),
            nombreCompleto = nombre.trim(),
            perfilOcupacion = perfil.trim(),
            interes = interes.trim()
        )
        storage.save(stored)
        return stored.toUsuario()
    }

    private fun StoredUser.toUsuario(): Usuario {
        val perfil = perfilOcupacion.ifBlank { "Creador de Contenido" }
        val topic = interes.ifBlank { "Muebles y Madera" }
        val initials = nombreCompleto.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString("") { it.first().uppercase() }
            .ifEmpty { "US" }

        return Usuario(
            id = id,
            username = username,
            password = "", // nunca se mantiene en memoria
            nombreCompleto = nombreCompleto,
            perfilOcupacion = perfil,
            avatarIniciales = initials,
            historialBusquedas = listOf(topic, "Lenga Patagónica", "Muebles Básicos", "Normativa NCh"),
            sugerenciasPersonalizadas = listOf(
                SugerenciaPersonalizada(
                    id = "sug_custom_1",
                    titulo = "Proyecto Personalizado: $topic",
                    descripcion = "Solución en madera desarrollada según tu perfil de $perfil.",
                    motivo = "Basado en tu registro e interés en '$topic'",
                    tipo = "Mueble / Proyecto",
                    badgeTexto = "Personalizado"
                ),
                SugerenciaPersonalizada(
                    id = "sug_custom_2",
                    titulo = "Guía de Selección de Madera para $topic",
                    descripcion = "Criterios según clima seco, húmedo y durabilidad biológica.",
                    motivo = "Basado en tu especialidad de $perfil",
                    tipo = "Material",
                    badgeTexto = "Recomendado"
                ),
                SugerenciaPersonalizada(
                    id = "sug_custom_3",
                    titulo = "Normativa y Cálculo de Diseño NCh",
                    descripcion = "Fichas técnicas validadas para estructuras en madera en Chile.",
                    motivo = "Recomendado para tu inicio en la app",
                    tipo = "Normativa",
                    badgeTexto = "Chile NCh"
                )
            )
        )
    }
}
