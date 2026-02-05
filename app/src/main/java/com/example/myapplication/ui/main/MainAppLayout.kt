package com.example.myapplication.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.drawer.drawerItems
import com.example.myapplication.ui.history.HistoryScreen
import com.example.myapplication.ui.payment.ManualCloseScreen
import com.example.myapplication.ui.payment.PaymentScreen
import com.example.myapplication.ui.settings.SettingsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppLayout() {
    // This navController manages screens INSIDE the drawer (Payment, Settings, etc.)
    val subNavController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by subNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "payment"

    val categorizedItems = remember {
        mapOf(
            "Payment" to drawerItems.slice(0..1),
            "History" to drawerItems.slice(2..4),
            "Settings" to drawerItems.slice(5 until drawerItems.size)
        )
    }

    val expandedStates = remember {
        mutableStateMapOf("Payment" to true, "History" to false, "Settings" to false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentRoute.replace("_", " ").uppercase()) },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        ModalNavigationDrawer(
            modifier = Modifier.padding(innerPadding),
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(drawerShape = RectangleShape, windowInsets = WindowInsets(0,0,0,0)) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
                        categorizedItems.forEach { (categoryName, items) ->
                            val isExpanded = expandedStates[categoryName] == true

                            // Category Header
                            CategoryHeader(categoryName, isExpanded) {
                                expandedStates[categoryName] = !isExpanded
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column {
                                    items.forEach { item ->
                                        val route = item.title.lowercase().replace(" ", "_")
                                        NavigationDrawerItem(
                                            label = { Text(item.title) },
                                            icon = { Icon(item.icon, contentDescription = null) },
                                            selected = currentRoute == route,
                                            onClick = {
                                                scope.launch { drawerState.close() }
                                                subNavController.navigate(route) {
                                                    popUpTo("payment") { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            },
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                    }
                }
            }
        ) {
            // Internal Navigation for Drawer Items
            NavHost(
                navController = subNavController,
                startDestination = "payment",
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {
                // Payment screens
                composable("payment") { PaymentScreen() }
                composable("manual_close") { ManualCloseScreen() }

                // History screens
                composable("history") { HistoryScreen() }
                // TODO: add remaining screens

                // Settings screens
                composable("settings") { SettingsScreen() }
                // TODO: add remaining screens
            }
        }
    }
}

@Composable
fun CategoryHeader(title: String, isExpanded: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Icon(imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
    }
}