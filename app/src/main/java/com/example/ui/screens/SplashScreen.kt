package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.mascot.LionMascot
import com.example.ui.components.mascot.LionPose
import com.example.ui.components.mascot.MakingBrandsLogoHeader
import com.example.ui.theme.TealDark
import com.example.ui.theme.TealDeepBg
import com.example.ui.theme.TealPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        val duration = 1500L
        val steps = 50
        val delayPerStep = duration / steps
        for (i in 1..steps) {
            delay(delayPerStep)
            progress = i / steps.toFloat()
        }
        delay(300)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(TealDeepBg, TealDark, Color(0xFF00383F))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Brand Header Logo
            MakingBrandsLogoHeader(
                logoSize = 56.dp,
                textColor = Color.White,
                subtitle = "Smart CRM for Smarter Businesses"
            )

            // Mascot Waving
            LionMascot(
                pose = LionPose.WAVING,
                modifier = Modifier.size(240.dp)
            )

            // Bottom Progress Bar matching prompt design
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                color = Color(0xFF004D54).copy(alpha = 0.85f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Loading your success...",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF2DD4BF),
                        trackColor = Color(0xFF003338)
                    )
                }
            }
        }
    }
}
