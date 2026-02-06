package com.example.myapplication.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.PaymentViewModel

const val SettingsRoute = "settings"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: PaymentViewModel) {
    // Dropdown states
    var currencyExpanded by remember { mutableStateOf(false) }
    var modeExpanded by remember { mutableStateOf(false) }

    // Mode selection (Local state for now)
    val modes = listOf(
        "General Purchase",
        "Public Transport (Navegante)",
        "Electricity/Gas Bill",
        "Mobile Top-up",
        "Tax Payment"
    )
    var selectedMode by remember { mutableStateOf(modes[0]) }

    // Toggle states
    var tipsEnabled by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- NEW SECTION: OPERATION MODE ---
        SettingsHeader("Operation Mode")

        ExposedDropdownMenuBox(
            expanded = modeExpanded,
            onExpandedChange = { modeExpanded = !modeExpanded },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = selectedMode,
                onValueChange = {},
                readOnly = true,
                label = { Text("Service Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = modeExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = modeExpanded, onDismissRequest = { modeExpanded = false }) {
                modes.forEach { mode ->
                    DropdownMenuItem(
                        text = { Text(mode) },
                        onClick = {
                            selectedMode = mode
                            modeExpanded = false
                        }
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // --- SECTION: PAYMENT CONFIGURATION ---
        SettingsHeader("Payment Configuration")

        ExposedDropdownMenuBox(
            expanded = currencyExpanded,
            onExpandedChange = { currencyExpanded = !currencyExpanded },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = "${viewModel.selectedCurrency.symbol} - ${viewModel.selectedCurrency.name}",
                onValueChange = {},
                readOnly = true,
                label = { Text("Default Currency") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = currencyExpanded, onDismissRequest = { currencyExpanded = false }) {
                viewModel.currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text("${currency.symbol} - ${currency.name}") },
                        onClick = {
                            viewModel.updateCurrency(currency)
                            currencyExpanded = false
                        }
                    )
                }
            }
        }

        SettingsToggle(
            title = "Enable Tips",
            subtitle = "Ask for gratuity before payment",
            checked = tipsEnabled,
            onCheckedChange = { tipsEnabled = it }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // --- SECTION: SECURITY ---
        SettingsHeader("Security")

        SettingsToggle(
            title = "Biometric Authentication",
            subtitle = "Use Fingerprint/FaceID to unlock app",
            checked = biometricEnabled,
            onCheckedChange = { biometricEnabled = it }
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        // --- SECTION: INFO (READ ONLY) ---
        SettingsHeader("Terminal Information")
        InfoRow(label = "Terminal ID", value = "TID-99284711")
        InfoRow(label = "Merchant ID", value = "MID-SOFT-POS-01")
        InfoRow(label = "App Version", value = "1.0.4 (Production)")
    }
}

@Composable
fun SettingsHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingsToggle(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}