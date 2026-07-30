package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.*

@Composable
fun WelcomeHomeScreen(
    onLoginClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    onGuestClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE6FFFA),
                        Color(0xFFF8FAFC),
                        Color.White
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Main Title
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Grow Your\nBusiness",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900,
                    lineHeight = 38.sp
                )
                Text(
                    text = "Smarter.",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TealPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Manage leads, track deals, automate tasks and close more deals — all in one place.",
                    fontSize = 14.sp,
                    color = Slate500,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Mascot Pointing at Floating Overview Card (Matching Image 1 Right Screen)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lion Mascot
                LionMascot(
                    pose = LionPose.POINTING,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(end = 4.dp)
                )

                // Sales Overview Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Sales Overview", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Slate100
                            ) {
                                Text(
                                    "This Month ▾",
                                    fontSize = 9.sp,
                                    color = Slate700,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // 3 Key Stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total Leads", fontSize = 9.sp, color = Slate500)
                                Text("1,248", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                Text("↑ 18.6%", fontSize = 9.sp, color = StageWon, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Won Deals", fontSize = 9.sp, color = Slate500)
                                Text("326", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                Text("↑ 24.8%", fontSize = 9.sp, color = StageWon, fontWeight = FontWeight.Bold)
                            }
                            Column {
                                Text("Revenue", fontSize = 9.sp, color = Slate500)
                                Text("₹ 24.80L", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate900)
                                Text("↑ 31.4%", fontSize = 9.sp, color = StageWon, fontWeight = FontWeight.Bold)
                            }
                        }

                        Divider(color = Slate100)

                        // Deals Pipeline Rows
                        Text("Deals Pipeline", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate900)
                        listOf(
                            "New" to (320 to StageNew),
                            "Qualified" to (245 to StageQualified),
                            "Proposal" to (128 to StageProposal),
                            "Won" to (32 to StageWon)
                        ).forEach { (stage, data) ->
                            val (valCount, color) = data
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(stage, fontSize = 8.sp, color = Slate500, modifier = Modifier.width(42.dp))
                                LinearProgressIndicator(
                                    progress = { valCount / 350f },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(6.dp)
                                        .clip(CircleShape),
                                    color = color,
                                    trackColor = Slate100
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("$valCount", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate800)
                            }
                        }

                        Divider(color = Slate100)

                        // Recent Activity Snippet
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                tint = TealPrimary,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Follow-up call with John Doe", fontSize = 9.sp, color = Slate700, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Action Buttons
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealDark)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Login", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Login",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onCreateAccountClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true)
            ) {
                Text("Create Account", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TealDark)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onGuestClick) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Explore as Guest", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Slate700)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Guest",
                        tint = Slate700,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
