package com.example.myapplication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.payment.Currency

class PaymentViewModel : ViewModel() {
    val currencies = listOf(
        Currency("EUR", "Euro", "€"),
        Currency("GBP", "Pound", "£"),
        Currency("USD", "Dollar", "$"),
        Currency("JPY", "Yen", "¥"),
        Currency("BRL", "Real", "R$"),
        Currency("CHF", "Franc", "Fr")
    )

    var selectedCurrency by mutableStateOf(currencies.first())
        private set

    fun updateCurrency(currency: Currency) {
        selectedCurrency = currency
    }
}