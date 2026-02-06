package com.example.myapplication.ui.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.myapplication.ui.PaymentViewModel
import java.util.Locale

const val PaymentRoute = "payment"
const val PaymentLoadingRoute = "payment_loading/{amount}"
const val PaymentQRRoute = "payment_qr/{amount}"

data class Currency(val code: String, val name: String, val symbol: String)

@Composable
fun PaymentScreen(viewModel: PaymentViewModel, onPay: (Long) -> Unit = {}) {
    var amountCents by remember { mutableLongStateOf(0L) }
    var expanded by remember { mutableStateOf(false) }

    fun appendDigits(digits: String) {
        val asString = amountCents.toString()
        if (asString.length + digits.length > 10) return
        amountCents = if (amountCents == 0L) digits.toLong() else (asString + digits).toLong()
    }

    fun eraseOne() {
        amountCents /= 10
    }

    // Logic updated to use ViewModel conversion
    fun formatAmountDisplay(cents: Long, locale: Locale): String {
        val rates = mapOf(
            "EUR" to 0.2412, "USD" to 0.25, "GBP" to 0.20,
            "JPY" to 37.50, "CHF" to 0.22, "BRL" to 1.25, "PLN" to 1.0
        )
        val rate = rates[viewModel.selectedCurrency.code] ?: 1.0
        val convertedValue = (cents / 100.0) * rate
        return String.format(locale, "%.2f", convertedValue)
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
                    text = viewModel.selectedCurrency.symbol,
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

            Spacer(modifier = Modifier.height(10.dp))

            // DCC DROPDOWN BUTTON
            // Wrap in a Column that fills the width and centers its children
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pay in your local currency?",
                    color = Color(0xFF1F51FF), // High-visibility Electric Blue
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Box(contentAlignment = Alignment.TopCenter) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(text = viewModel.selectedCurrency.code)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        viewModel.currencies.filter { it != viewModel.selectedCurrency }.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text("${currency.name} (${currency.code})") },
                                onClick = {
                                    viewModel.updateCurrency(currency)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

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
                onClick = { if (amountCents > 0) onPay(amountCents) },
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth().height(72.dp)
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
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (label == "X") {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Text(text = label, fontSize = 26.sp, color = Color.Black)
            }
        }
    }
}