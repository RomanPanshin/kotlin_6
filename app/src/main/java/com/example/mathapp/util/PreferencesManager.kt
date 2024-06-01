package com.example.mathapp.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("search_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }

    fun saveSearchQuery(query: String) {
        val history = getSearchHistory().toMutableList()
        history.remove(query)  // Remove if exists to avoid duplication
        history.add(0, query)  // Add to top
        if (history.size > MAX_HISTORY_SIZE) {
            history.removeAt(history.size - 1)  // Remove oldest entry
        }
        prefs.edit().putStringSet(SEARCH_HISTORY_KEY, history.toSet()).apply()
    }

    fun getSearchHistory(): List<String> {
        return prefs.getStringSet(SEARCH_HISTORY_KEY, emptySet())?.toList() ?: emptyList()
    }

    fun clearSearchHistory() {
        prefs.edit().remove(SEARCH_HISTORY_KEY).apply()
    }
}
