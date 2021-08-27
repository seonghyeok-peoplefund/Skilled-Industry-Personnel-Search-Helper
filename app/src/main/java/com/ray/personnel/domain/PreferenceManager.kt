package com.ray.personnel.domain

import android.content.Context
import androidx.core.content.edit

// 완전히 가져옴. 수정한 내용 1도 없음
object PreferenceManager {
    private const val PREFERENCES_NAME = "preference_personnel"

    private const val DEFAULT_VALUE_STRING = ""

    private fun getPreferences(context: Context) = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setString(context: Context, key: String, value: String) {
        getPreferences(context).edit {
            putString(key, value)
        }
    }

    fun getString(context: Context, key: String): String {
        return getPreferences(context).getString(key, DEFAULT_VALUE_STRING) ?: DEFAULT_VALUE_STRING
    }
}