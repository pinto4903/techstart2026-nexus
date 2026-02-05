package com.example.myapplication.ui.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

// 1. Keep your route constant for the NavHost
const val PaymentRoute = "payment"

data class Currency(val code: String, val name: String, val symbol: String)

@Composable
fun PaymentScreen(selectedCurrency: Currency, onPay: (Long) -> Unit = {}) {
    var amountCents by remember { mutableLongStateOf(0L) }

    fun appendDigits(digits: String) {
        val asString = amountCents.toString()
        if (asString.length + digits.length > 10) return
        amountCents = if (amountCents == 0L) digits.toLong() else (asString + digits).toLong()
    }

    fun eraseOne() {
        amountCents /= 10
    }

    fun formatAmountDisplay(cents: Long, locale: Locale): String {
        val units = cents / 100
        val rem = (cents % 100).toInt()
        return String.format(locale, "%d,%02d", units, rem)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = selectedCurrency.symbol, // Now dynamic from Settings!
                    fontSize = 45.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 15.dp),
                )

                Text(
                    text = formatAmountDisplay(amountCents, Locale.getDefault()),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
            }
        }

        // Keypad Block
        Column(modifier = Modifier.fillMaxWidth()) {
            @Composable
            fun KeyRow(vararg labels: String) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    labels.forEach { label ->
                        Box(modifier = Modifier.weight(1f)) {
                            KeyButton(label = label, onClick = {
                                when (label) {
                                    "X" -> eraseOne()
                                    "00" -> appendDigits("00")
                                    else -> appendDigits(label)
                                }
                            })
                        }
                    }
                }
            }

            KeyRow("1", "2", "3")
            KeyRow("4", "5", "6")
            KeyRow("7", "8", "9")
            KeyRow("00", "0", "X")

            Button(
                onClick = { onPay(amountCents) },
                shape = RectangleShape, // Flush with the bottom of the screen
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Text(text = "PAY", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun KeyButton(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White,
        border = BorderStroke(0.2.dp, Color.LightGray),
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (label == "X") {
                // Render the Backspace Icon instead of text
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                // Render the number or "00"
                Text(
                    text = label,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
        }
    }
}