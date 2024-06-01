// File: SearchScreen.kt

package com.example.mathapp.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.mathapp.R
import com.example.mathapp.service.TheoremService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.compose.runtime.saveable.rememberSaveable
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    service: TheoremService,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var searchText by rememberSaveable { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var lastQuery by remember { mutableStateOf<String?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val historyKey = stringSetPreferencesKey("search_history")
    val dataStore = context.dataStore

    var searchHistory by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        searchHistory = dataStore.data.first()[historyKey]?.toList() ?: emptyList()
    }

    fun updateHistory(query: String) {
        coroutineScope.launch {
            val updatedHistory = (listOf(query) + searchHistory).distinct().take(10)
            dataStore.edit { settings ->
                settings[historyKey] = updatedHistory.toSet()
            }
            searchHistory = updatedHistory
        }
    }

    fun clearHistory() {
        coroutineScope.launch {
            dataStore.edit { settings ->
                settings[historyKey] = emptySet()
            }
            searchHistory = emptyList()
        }
    }

    suspend fun fetchTheorem(query: String): Result<String?> {
        return try {
            val response = service.getTheorem(query)
            if (response.isSuccessful) {
                Result.success(response.body()?.theorem)
            } else {
                Result.failure(Exception("Error: ${response.message()}"))
            }
        } catch (e: IOException) {
            Result.failure(Exception(context.getString(R.string.network_error)))
        } catch (e: HttpException) {
            Result.failure(Exception(context.getString(R.string.server_error)))
        }
    }

    fun performSearch(query: String) {
        searchResult = null
        errorMessage = null
        lastQuery = query
        isSearching = true

        coroutineScope.launch {
            // Ensure the ProgressBar is visible for at least 30 ms
            delay(30)
            val result = fetchTheorem(query)
            isSearching = false
            result.onSuccess {
                searchResult = it
                if (searchResult.isNullOrEmpty()) {
                    errorMessage = context.getString(R.string.no_results_found)
                } else {
                    updateHistory(query)
                }
            }.onFailure {
                errorMessage = it.message
            }
        }
    }

    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            delay(2000)
            performSearch(searchText)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.search_title)) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.dark_mode))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = onThemeChange
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            // Image layer
            Image(
                painter = painterResource(id = R.drawable.squaredpage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                colorFilter = if (!isDarkTheme) ColorFilter.tint(Color(0xFF5C075E)) else null,
                modifier = Modifier.fillMaxSize()
            )

            // Content layer
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(id = R.dimen.padding_16)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(stringResource(id = R.string.search_hint)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            performSearch(searchText)
                        }
                    ),
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                searchText = ""
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_clear),
                                    contentDescription = stringResource(id = R.string.clear)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isSearching) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xAA000000)), // Semi-transparent background
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                if (searchText.isEmpty() && searchHistory.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(text = stringResource(id = R.string.search_history), style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        for (query in searchHistory) {
                            Text(
                                text = query,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        searchText = query
                                        performSearch(query)
                                    }
                                    .padding(vertical = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { clearHistory() }) {
                            Text(stringResource(id = R.string.clear_history))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    searchResult != null -> {
                        Text(text = searchResult!!)
                    }
                    errorMessage != null -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = errorMessage!!)
                            if (errorMessage == context.getString(R.string.network_error) ||
                                errorMessage == context.getString(R.string.server_error)) {
                                Button(onClick = { performSearch(lastQuery ?: "") }) {
                                    Text(stringResource(id = R.string.update))
                                }
                            }
                        }
                    }
                    else -> {
                        Text(text = stringResource(id = R.string.please_enter_theorem))
                    }
                }
            }
        }
    }
}

// Extension function to get DataStore
val Context.dataStore by preferencesDataStore(name = "search_preferences")
