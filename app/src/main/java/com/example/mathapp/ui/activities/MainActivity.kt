package com.example.mathapp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.mathapp.R
import com.example.mathapp.ui.themes.MathAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            MathAppTheme(darkTheme = isDarkTheme) {
                MainScreen(isDarkTheme) { isDarkTheme = !isDarkTheme }
            }
        }
    }

    @Composable
    fun MainScreen(isDarkTheme: Boolean, onThemeChange: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Background color layer
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
            )

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
                Text(
                    text = stringResource(id = R.string.mathtab_string),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkTheme) Color.White else Color(0xFF5C075E)
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_48)))
                Image(
                    painter = painterResource(id = R.drawable.img),
                    contentDescription = null,
                    colorFilter = if (!isDarkTheme) ColorFilter.tint(Color(0xFF5C075E)) else null,
                    modifier = Modifier
                        .width(dimensionResource(id = R.dimen.image_width))
                        .height(dimensionResource(id = R.dimen.image_height))
                        .padding(vertical = dimensionResource(id = R.dimen.padding_16)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_96)))
                Text(
                    text = stringResource(id = R.string.hi_string),
                    fontSize = 40.sp,
                    color = if (isDarkTheme) Color.White else Color.Black,
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.height_36)))
                Text(
                    text = stringResource(id = R.string.this_app_will_help_you_solve_complex_equations_string),
                    fontSize = 20.sp,
                    color = if (isDarkTheme) Color.White else Color(0xFF5C075E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_16))
                )
            }

            // Top-right buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_16)),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(this@MainActivity, Calculator::class.java)
                        startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.fi),
                        contentDescription = null,
                        modifier = Modifier.size(
                            dimensionResource(id = R.dimen.icon_size_width),
                            dimensionResource(id = R.dimen.icon_size_height)
                        ),
                        tint = if (isDarkTheme) Color.White else Color(0xFF5C075E)
                    )
                }
                IconButton(
                    onClick = {
                        val intent = Intent(this@MainActivity, SearchActivity::class.java)
                        startActivity(intent)
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(id = R.string.search),
                        modifier = Modifier.size(
                            dimensionResource(id = R.dimen.icon_size_width),
                            dimensionResource(id = R.dimen.icon_size_height)
                        ),
                        tint = if (isDarkTheme) Color.White else Color(0xFF5C075E)
                    )
                }
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { onThemeChange() },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
                )
            }
        }
    }
}
