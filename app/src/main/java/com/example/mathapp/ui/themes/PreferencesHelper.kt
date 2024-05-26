package com.example.mathapp.ui.theme

import android.content.Context

object PreferencesHelper {
    private const val PREFS_NAME = "theme_preferences"
    private const val PREF_THEME_KEY = "theme_dark"

    fun setDarkTheme(context: Context, isDark: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(PREF_THEME_KEY, isDark)
            .apply()
    }

    fun isDarkTheme(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(PREF_THEME_KEY, false) // Default to false (light theme)
}
