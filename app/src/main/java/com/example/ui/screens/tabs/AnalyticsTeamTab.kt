package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LeadEntity
import com.example.data.LeadSource
import com.example.data.LeadStage
import com.example.data.TeamMemberEntity
import com.example.ui.theme.*

@Composable
fun AnalyticsTeamTab(
    leads: List<LeadEntity>,
    teamMembers: List<TeamMemberEntity>,
    onExportCsv: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Analytics & Team Insights", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    Text("Real-time performance and team leaderboard", fontSize = 12.sp, color = Slate500)
                }
                Button(
                    onClick = onExportCsv,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Icon(Icons.Default.Download, contentDescription = "Export CSV", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Conversion Funnel Performance Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Conversion Rate by Stage", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    Spacer(modifier = Modifier.height(12.dp))

                    val stages = listOf(
                        "New → Qualified" to 76.5f,
                        "Qualified → Proposal" to 52.2f,
                        "Proposal → Negotiation" to 50.0f,
                        "Negotiation → Won" to 50.0f
                    )

                    stages.forEach { (label, pct) ->
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(label, fontSize = 12.sp, color = Slate800, fontWeight = FontWeight.Medium)
                                Text("${pct.toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { pct / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = TealPrimary,
                                trackColor = Slate100
                            )
                        }
                    }
                }
            }
        }

        // Lead Sources Breakdown Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Lead Sources Breakdown", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    Spacer(modifier = Modifier.height(12.dp))

                    LeadSource.values().forEach { source ->
                        val sourceCount = leads.count { it.source == source }
                        val pct = if (leads.isEmpty()) 0f else (sourceCount.toFloat() / leads.size) * 100f
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(source.label, fontSize = 13.sp, color = Slate800, fontWeight = FontWeight.Medium)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("$sourceCount leads", fontSize = 12.sp, color = Slate500)
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = TealLightBg
                                ) {
                                    Text(
                                        "${pct.toInt()}%",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TealPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Team Leaderboard Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = StageNegotiation)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Team Performance Leaderboard", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                }
            }
        }

        items(teamMembers) { member ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(TealLightBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                member.name.first().toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TealPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(member.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text(member.role, fontSize = 12.sp, color = Slate500)
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "₹${(member.revenue / 100000.0).formatOneDecimal()} Lakhs",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StageWon
                        )
                        Text("${member.dealsClosed} deals closed", fontSize = 11.sp, color = Slate500)
                    }
                }
            }
        }
    }
}
