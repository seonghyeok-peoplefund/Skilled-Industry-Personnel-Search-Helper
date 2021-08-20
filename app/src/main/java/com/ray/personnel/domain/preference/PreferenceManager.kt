package com.ray.personnel.domain.preference

import android.content.Context

object PreferenceManager {
    const val PREFERENCES_NAME = "preference_personnel"
    private const val DEFAULT_VALUE_STRING = ""
    private const val DEFAULT_VALUE_INT = -1

    // ApplicationContext는 MemoryLeak 를 불러올 수 있으니 지양할 것.
    private fun getPreferences(context: Context) = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setString(context: Context, key: String, value: String) {
        with(getPreferences(context).edit()) {
            putString(key, value)
            apply()
        }
    }

    fun setInt(context: Context, key: String, value: Int) {
        with(getPreferences(context).edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getString(context: Context, key: String): String {
        return getPreferences(context)
            .getString(key, DEFAULT_VALUE_STRING)
            ?: ""
    }

    fun getInt(context: Context, key: String): Int {
        return getPreferences(context)
            .getInt(key, DEFAULT_VALUE_INT)
    }
}