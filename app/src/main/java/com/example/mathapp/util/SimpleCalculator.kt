// File: SimpleCalculator.kt

package com.example.mathapp.util

import java.lang.Exception
import java.util.Stack

class SimpleCalculator {

    fun evaluate(expression: String): String {
        return try {
            val result = evaluateExpression(expression)
            result.toString()
        } catch (e: Exception) {
            "Error"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val tokens = expression.toCharArray()

        val values = Stack<Double>()
        val operators = Stack<Char>()

        var i = 0
        while (i < tokens.size) {
            when {
                tokens[i] == ' ' -> {
                    i++
                }
                tokens[i] in '0'..'9' || tokens[i] == '.' -> {
                    val sb = StringBuilder()
                    while (i < tokens.size && (tokens[i] in '0'..'9' || tokens[i] == '.')) {
                        sb.append(tokens[i++])
                    }
                    values.push(sb.toString().toDouble())
                }
                tokens[i] == '(' -> {
                    operators.push(tokens[i])
                    i++
                }
                tokens[i] == ')' -> {
                    while (operators.peek() != '(') {
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()))
                    }
                    operators.pop()
                    i++
                }
                tokens[i] in listOf('+', '-', '*', '/') -> {
                    while (!operators.isEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                        values.push(applyOperator(operators.pop(), values.pop(), values.pop()))
                    }
                    operators.push(tokens[i])
                    i++
                }
                else -> {
                    throw IllegalArgumentException("Invalid character in expression")
                }
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()))
        }

        return values.pop()
    }

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') {
            return false
        }
        return !(op1 == '*' || op1 == '/') || (op2 != '+' && op2 != '-')
    }

    private fun applyOperator(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> {
                if (b == 0.0) {
                    throw UnsupportedOperationException("Cannot divide by zero")
                }
                a / b
            }
            else -> throw IllegalArgumentException("Unsupported operator")
        }
    }
}
