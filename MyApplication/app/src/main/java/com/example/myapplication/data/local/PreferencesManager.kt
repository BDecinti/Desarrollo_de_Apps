package com.example.myapplication.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences? = try {
        context.getSharedPreferences("disena_madera_prefs", Context.MODE_PRIVATE)
    } catch (_: Exception) {
        null
    }

    companion object {
        private const val KEY_FAVORITE_IDS = "key_favorite_ids"
        private const val KEY_LOGGED_USERNAME = "key_logged_username"
        private const val KEY_CLIMATE_ZONE = "key_climate_zone"
    }

    fun getFavoriteIds(): Set<String> {
        val saved = prefs?.getStringSet(KEY_FAVORITE_IDS, null)
        return saved ?: setOf("ft_001", "ft_003") // Default initial demo favorites
    }

    fun saveFavoriteIds(ids: Set<String>) {
        prefs?.edit()?.putStringSet(KEY_FAVORITE_IDS, ids)?.apply()
    }

    fun getLoggedUsername(): String? {
        return prefs?.getString(KEY_LOGGED_USERNAME, null)
    }

    fun saveLoggedUsername(username: String?) {
        if (username != null) {
            prefs?.edit()?.putString(KEY_LOGGED_USERNAME, username)?.apply()
        } else {
            prefs?.edit()?.remove(KEY_LOGGED_USERNAME)?.apply()
        }
    }

    fun getClimateZoneLabel(): String? = prefs?.getString(KEY_CLIMATE_ZONE, null)

    fun saveClimateZoneLabel(label: String?) {
        if (label != null) {
            prefs?.edit()?.putString(KEY_CLIMATE_ZONE, label)?.apply()
        } else {
            prefs?.edit()?.remove(KEY_CLIMATE_ZONE)?.apply()
        }
    }

    fun clearAll() {
        prefs?.edit()?.clear()?.apply()
    }
}
