package com.example.myapplication.ui.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

// Modelo de Moeda
data class Currency(val code: String, val name: String, val symbol: String)

@Composable
@Preview(showBackground = true)
fun PaymentScreen(onPay: (Long) -> Unit = {}) {
    val currencies = listOf(
        Currency("EUR", "Euro", "€"),
        Currency("GBP", "Pound", "£"),
        Currency("USD", "Dollar", "$"),
        Currency("JPY", "Yen", "¥"),
        Currency("BRL", "Real", "R$"),
        Currency("CHF", "Franc", "Fr")
    )

    var selectedCurrency by remember { mutableStateOf(currencies.first()) }
    var isCurrencyMenuExpanded by remember { mutableStateOf(false) }
    var amountCents by remember { mutableStateOf(0L) }

    // Lógica do Teclado
    fun appendDigits(digits: String) {
        val asString = amountCents.toString()
        if (asString.length + digits.length > 12) return
        amountCents = (asString + digits).toLong()
    }

    fun eraseOne() {
        amountCents /= 10
    }

    fun formatAmountDisplay(cents: Long, locale: Locale): String {
        val units = cents / 100
        val rem = (cents % 100).toInt()
        // Formata apenas os números, o símbolo vem do seletor
        return String.format(locale, "%d,%02d", units, rem)
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {

            // 1. Área do Valor e Seletor de Moeda
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Seletor de Moeda Clicável
                Box {
                    Row(
                        modifier = Modifier
                            .clickable { isCurrencyMenuExpanded = true }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedCurrency.symbol,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB00020) // Cor de destaque para o símbolo
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Select Currency",
                            tint = Color.Gray
                        )
                    }

                    // Menu Dropdown com Scroll
                    DropdownMenu(
                        expanded = isCurrencyMenuExpanded,
                        onDismissRequest = { isCurrencyMenuExpanded = false },
                        modifier = Modifier.height(200.dp) // Altura fixa para habilitar scroll interno
                    ) {
                        currencies.forEach { currency ->
                            DropdownMenuItem(
                                text = { Text("${currency.symbol} - ${currency.name}") },
                                onClick = {
                                    selectedCurrency = currency
                                    isCurrencyMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Display do Valor Numérico
                Text(
                    text = formatAmountDisplay(amountCents, Locale.getDefault()),
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Black
                )
            }

            // 2. Bloco Unificado: Teclado + Botão Pay
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB00020),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                ) {
                    Text(
                        text = "PAY",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun KeyButton(label: String, onClick: () -> Unit) {
    val display = if (label == "X") "✕" else label

    Surface(
        onClick = onClick,
        color = Color.White,
        border = BorderStroke(0.4.dp, Color.LightGray),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = display,
                fontSize = 26.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black
            )
        }
    }
}
