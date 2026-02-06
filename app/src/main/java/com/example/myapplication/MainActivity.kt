package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.login.LoginScreen
import com.example.myapplication.ui.main.MainAppLayout
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val rootNavController = rememberNavController()

                // The Root NavHost controls the flow between Login and the App
                NavHost(navController = rootNavController, startDestination = "login") {

                    composable("login") {
                        LoginScreen(onLoginSuccess = {
                            // Navigate to main and clear login from the backstack
                            rootNavController.navigate("main_app") {
                                popUpTo("login") { inclusive = true }
                            }
                        })
                    }

                    composable("main_app") {
                        // This is the "Shell" that contains the Drawer and its own internal NavHost
                        MainAppLayout()
                    }
                }
            }
        }
    }
}