// File: CalculatorActivity.kt

package com.example.mathapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mathapp.ui.screens.CalculatorScreen
import com.example.mathapp.ui.themes.MathAppTheme

class Calculator : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            MathAppTheme(darkTheme = isDarkTheme) {
                CalculatorScreen(isDarkTheme = isDarkTheme, onThemeChange = { isDarkTheme = !isDarkTheme })
            }
        }
    }
}
