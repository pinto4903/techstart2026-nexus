package com.example.myapplication.ui.payment

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentLoadingScreen(onTimeout: () -> Unit = {}) {
    val infiniteTransition = rememberInfiniteTransition(label = "loadingAnimation")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Call onTimeout after 30 seconds (configurable) to handle long waits
    LaunchedEffect(Unit) {
        delay(2_500L)
        onTimeout()
    }

    Scaffold{ paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = Color(0xFFF2F2F7)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Updating card reader, please wait. The operation will proceed after the update.",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(60.dp))

                Icon(
                    imageVector = Icons.Default.HourglassEmpty,
                    contentDescription = "Loading",
                    modifier = Modifier
                        .size(100.dp)
                        .graphicsLayer {
                            rotationZ = rotation
                        },
                    tint = Color(0xFF6650a4)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentLoadingScreenPreview() {
    PaymentLoadingScreen()
}