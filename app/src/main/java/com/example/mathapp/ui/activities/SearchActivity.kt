// File: SearchActivity.kt

package com.example.mathapp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.mathapp.service.TheoremService
import com.example.mathapp.ui.screens.SearchScreen
import com.example.mathapp.ui.themes.MathAppTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : ComponentActivity() {
    private val logging = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
    private val client = OkHttpClient.Builder().addInterceptor(logging).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:4000/") // Use this IP to connect to localhost from an emulator
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(TheoremService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }

            MathAppTheme(darkTheme = isDarkTheme) {
                SearchScreen(
                    onBack = { onBackPressedDispatcher.onBackPressed() },
                    service = service,
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it }
                )
            }
        }
    }
}
