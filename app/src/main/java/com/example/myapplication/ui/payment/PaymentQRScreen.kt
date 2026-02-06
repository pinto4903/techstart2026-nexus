package com.example.myapplication.ui.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import java.util.Locale

@Composable
fun PaymentQRScreen(amount: Long, onCancel: () -> Unit) {
    val formattedAmount = remember(amount) {
        val units = amount / 100
        val rem = amount % 100
        String.format(Locale.GERMANY, "%d,%02d €", units, rem)
    }

    Scaffold(
        containerColor = Color(0xFFF2F2F7),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = formattedAmount,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black,
                modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
            )

            Text(
                text = "Pague com o MB WAY ou\nApresente o cartão",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                color = Color.DarkGray,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF34C759)))
                repeat(4) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFC7C7CC)))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = Color.White
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.qr_code),
                        contentDescription = "Payment QR Code",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Updated Payment Provider Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // MB WAY Logo Replacement
                Image(
                    painter = painterResource(id = R.drawable.mbway), // Ensure file is named mbway.png in drawable
                    contentDescription = "MB WAY Logo",
                    modifier = Modifier
                        .height(100.dp) // Adjusted to fit the text height
                        .width(IntrinsicSize.Min),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(16.dp))
                Text("MB", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.width(16.dp))
                Text("VISA", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1A1F71))
                Spacer(modifier = Modifier.width(16.dp))
                Text("MC", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFFEB001B))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pagar com o número de telemóvel",
                color = Color(0xFF007AFF),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                modifier = Modifier.clickable { /* Handle Phone Payment */ }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { /* TODO: Handle Card payment */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Icon(Icons.Default.CreditCard, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("CARTÃO", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.5.dp, Color(0xFFE53935))
            ) {
                Text("Cancelar", color = Color(0xFFE53935), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}