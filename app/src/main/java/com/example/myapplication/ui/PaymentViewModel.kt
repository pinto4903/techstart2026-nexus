package com.example.myapplication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.payment.Currency

const val uprate = 1.005
class PaymentViewModel : ViewModel() {

    val currencies = listOf(
        Currency("PLN", "Zloty", "Zł"),
        Currency("EUR", "Euro", "€"),
        Currency("USD", "Dollar", "$"),
        Currency("GBP", "Pound", "£"),
        Currency("JPY", "Yen", "¥"),
        Currency("CHF", "Franc", "Fr"),
        Currency("BRL", "Real", "R$")
    )

    var selectedCurrency by mutableStateOf(currencies.first()) // Starts as PLN
        private set

    // This stores the multiplier relative to 1 PLN
    var currentRate by mutableStateOf(1.0)
        private set

    var currentValue by mutableStateOf(0L)

    fun updateCurrency(newCurrency: Currency) {
        // Rates relative to 1 PLN (Example rates)
        currentRate = when (newCurrency.code) {
            "EUR" -> 0.2412
            "USD" -> 0.25
            "GBP" -> 0.20
            "JPY" -> 37.50
            "CHF" -> 0.22
            "BRL" -> 1.25
            else -> 1.0 // Default for PLN
        }
        selectedCurrency = newCurrency
    }
}