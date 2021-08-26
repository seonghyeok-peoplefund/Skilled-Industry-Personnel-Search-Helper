package com.ray.personnel.domain

import android.content.Context
import androidx.core.content.edit

object PreferenceManager {
    private const val PREFERENCES_NAME = "preference_personnel"
    private const val DEFAULT_VALUE_STRING = ""
    private const val DEFAULT_VALUE_INT = -1

    private fun getPreferences(context: Context) = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setString(context: Context, key: String, value: String) {
        getPreferences(context).edit {
            putString(key, value)
        }
    }

    fun setInt(context: Context, key: String, value: Int) {
        getPreferences(context).edit {
            putInt(key, value)
        }
    }

    fun getString(context: Context, key: String): String {
        return getPreferences(context).getString(key, DEFAULT_VALUE_STRING)!!
    }

    fun getInt(context: Context, key: String): Int {
        return getPreferences(context).getInt(key, DEFAULT_VALUE_INT)
    }
}