package com.example.myapplication.ui.drawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SpeakerPhone
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.ui.graphics.vector.ImageVector

data class DrawerItem(
    val title: String,
    val icon: ImageVector
)

val drawerItems = listOf(
    DrawerItem("Payment", Icons.Filled.CreditCard),
    DrawerItem("Manual Close", Icons.Filled.Lock),
    DrawerItem("History", Icons.Filled.History),
    DrawerItem("Search Transaction", Icons.Filled.Search),
    DrawerItem("Pending Transactions", Icons.Filled.PendingActions),
    DrawerItem("Settings", Icons.Filled.Settings),
    DrawerItem("Security", Icons.Filled.Security),
    DrawerItem("Manage Card Readers", Icons.Filled.SpeakerPhone),
    DrawerItem("Manage TPA", Icons.Filled.SpeakerPhone),
    DrawerItem("Change Mobile", Icons.Filled.Phone),
    DrawerItem("Change Password", Icons.Filled.VpnKey),
    DrawerItem("Support", Icons.Filled.HeadsetMic),
    DrawerItem("Terms and Conditions", Icons.AutoMirrored.Filled.Article)
)
