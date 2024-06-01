// File: CalculatorScreen.kt

package com.example.mathapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathapp.R
import com.example.mathapp.util.SimpleCalculator

@Composable
fun CalculatorScreen(isDarkTheme: Boolean, onThemeChange: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val textColor = if (isDarkTheme) Color.White else Color(0xFF5C075E)
    val buttonBackgroundColor = if (isDarkTheme) Color(0xFF5C075E) else Color.White
    val buttonTextColor = if (isDarkTheme) Color.White else Color.Black
    val buttonBorderColor = if (isDarkTheme) Color.White else Color(0xFF5C075E)

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
                .padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = input,
                    color = textColor,
                    fontSize = dimensionResource(id = R.dimen.text_size_input).value.sp,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = result,
                    color = textColor,
                    fontSize = dimensionResource(id = R.dimen.text_size_result).value.sp,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                    fontWeight = FontWeight.Bold
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                CalculatorButtonRow(
                    buttons = listOf(
                        stringResource(R.string.ce),
                        stringResource(R.string.divide),
                        stringResource(R.string.i)
                    ),
                    onButtonClick = { button -> handleButtonClick(button, input, result, { newInput -> input = newInput }, { newResult -> result = newResult }) },
                    buttonBackgroundColor = buttonBackgroundColor,
                    buttonTextColor = buttonTextColor,
                    buttonBorderColor = buttonBorderColor
                )
                CalculatorButtonRow(
                    buttons = listOf(
                        stringResource(R.string.seven),
                        stringResource(R.string.eight),
                        stringResource(R.string.nine),
                        stringResource(R.string.multiply)
                    ),
                    onButtonClick = { button -> handleButtonClick(button, input, result, { newInput -> input = newInput }, { newResult -> result = newResult }) },
                    buttonBackgroundColor = buttonBackgroundColor,
                    buttonTextColor = buttonTextColor,
                    buttonBorderColor = buttonBorderColor
                )
                CalculatorButtonRow(
                    buttons = listOf(
                        stringResource(R.string.four),
                        stringResource(R.string.five),
                        stringResource(R.string.six),
                        stringResource(R.string.subtract)
                    ),
                    onButtonClick = { button -> handleButtonClick(button, input, result, { newInput -> input = newInput }, { newResult -> result = newResult }) },
                    buttonBackgroundColor = buttonBackgroundColor,
                    buttonTextColor = buttonTextColor,
                    buttonBorderColor = buttonBorderColor
                )
                CalculatorButtonRow(
                    buttons = listOf(
                        stringResource(R.string.one),
                        stringResource(R.string.two),
                        stringResource(R.string.three),
                        stringResource(R.string.add)
                    ),
                    onButtonClick = { button -> handleButtonClick(button, input, result, { newInput -> input = newInput }, { newResult -> result = newResult }) },
                    buttonBackgroundColor = buttonBackgroundColor,
                    buttonTextColor = buttonTextColor,
                    buttonBorderColor = buttonBorderColor
                )
                CalculatorButtonRow(
                    buttons = listOf(
                        stringResource(R.string.dot),
                        stringResource(R.string.zero),
                        stringResource(R.string.back),
                        stringResource(R.string.equals)
                    ),
                    onButtonClick = { button -> handleButtonClick(button, input, result, { newInput -> input = newInput }, { newResult -> result = newResult }) },
                    buttonBackgroundColor = buttonBackgroundColor,
                    buttonTextColor = buttonTextColor,
                    buttonBorderColor = buttonBorderColor
                )
            }
        }

        // Top-right buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            horizontalAlignment = Alignment.End
        ) {
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeChange() },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White)
            )
        }
    }
}

@Composable
fun CalculatorButtonRow(buttons: List<String>, onButtonClick: (String) -> Unit, buttonBackgroundColor: Color, buttonTextColor: Color, buttonBorderColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        buttons.forEach { button ->
            Text(
                text = button,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onButtonClick(button) }
                    .border(1.dp, buttonBorderColor)  // thinner border
                    .background(buttonBackgroundColor)
                    .padding(vertical = dimensionResource(id = R.dimen.padding_medium)),
                color = buttonTextColor,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun handleButtonClick(
    button: String,
    input: String,
    result: String,
    updateInput: (String) -> Unit,
    updateResult: (String) -> Unit
) {
    when (button) {
        "CE" -> updateInput("")
        "=" -> updateResult(calculateResult(input))
        "Back" -> updateInput(input.dropLast(1))
        else -> updateInput(input + button)
    }
}

private fun calculateResult(input: String): String {
    val calculator = SimpleCalculator()
    return calculator.evaluate(input)
}
