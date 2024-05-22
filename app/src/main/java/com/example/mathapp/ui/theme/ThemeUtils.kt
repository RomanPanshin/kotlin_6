package com.example.mathapp.ui.theme
import android.content.Context

object ThemeUtils {
    private const val PREFS_NAME = "theme_preferences"
    private const val PREF_THEME_KEY = "theme_dark"

    fun setDarkTheme(context: Context, isDark: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(PREF_THEME_KEY, isDark).apply()
    }

    fun isDarkTheme(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(PREF_THEME_KEY, false) // Default to light theme
    }
}
