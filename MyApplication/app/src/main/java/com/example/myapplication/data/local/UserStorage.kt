package com.example.myapplication.data.local

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Registro persistido de un usuario creado desde la app (la contraseña se guarda como hash con sal). */
data class StoredUser(
    val id: String,
    val username: String,
    val passwordHash: String,
    val nombreCompleto: String,
    val perfilOcupacion: String,
    val interes: String
)

/**
 * Persistencia local de los usuarios registrados (SharedPreferences + JSON).
 */
class UserStorage(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getAll(): List<StoredUser> {
        val raw = prefs.getString(KEY_USERS, null) ?: return emptyList()
        return try {
            val array = JSONArray(raw)
            (0 until array.length()).map { i ->
                val o = array.getJSONObject(i)
                StoredUser(
                    id = o.getString("id"),
                    username = o.getString("username"),
                    passwordHash = o.getString("passwordHash"),
                    nombreCompleto = o.getString("nombreCompleto"),
                    perfilOcupacion = o.optString("perfilOcupacion"),
                    interes = o.optString("interes")
                )
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun find(username: String): StoredUser? =
        getAll().firstOrNull { it.username.equals(username.trim(), ignoreCase = true) }

    fun save(user: StoredUser) {
        val updated = getAll().filterNot { it.username.equals(user.username, ignoreCase = true) } + user
        val array = JSONArray()
        updated.forEach { u ->
            array.put(
                JSONObject()
                    .put("id", u.id)
                    .put("username", u.username)
                    .put("passwordHash", u.passwordHash)
                    .put("nombreCompleto", u.nombreCompleto)
                    .put("perfilOcupacion", u.perfilOcupacion)
                    .put("interes", u.interes)
            )
        }
        prefs.edit().putString(KEY_USERS, array.toString()).apply()
    }

    private companion object {
        const val PREFS_NAME = "disena_madera_users"
        const val KEY_USERS = "key_registered_users"
    }
}
