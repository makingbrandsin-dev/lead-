package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*

@Composable
fun DashboardTab(
    leads: List<LeadEntity>,
    activities: List<ActivityEntity>,
    onAddLeadClick: () -> Unit,
    onLeadClick: (LeadEntity) -> Unit,
    onNavigateToLeads: () -> Unit,
    onNavigateToPipeline: () -> Unit,
    onNavigateToAutomation: () -> Unit
) {
    val totalLeads = leads.size
    val totalRevenue = leads.filter { it.stage == LeadStage.WON }.sumOf { it.dealValue }
    val pipelineValue = leads.filter { it.stage != LeadStage.WON && it.stage != LeadStage.LOST }.sumOf { it.dealValue }
    val wonDeals = leads.count { it.stage == LeadStage.WON }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner / Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = TealDark)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Making Brands Smart CRM",
                        fontSize = 12.sp,
                        color = TealAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Welcome back, Sales Manager!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Your pipeline is up 24.8% this month.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Metrics Grid (4 cards)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Total Leads",
                        value = "$totalLeads",
                        trend = "+18.6%",
                        color = StageNew,
                        icon = Icons.Default.People
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Won Deals",
                        value = "$wonDeals",
                        trend = "+24.8%",
                        color = StageWon,
                        icon = Icons.Default.EmojiEvents
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Won Revenue",
                        value = "₹${(totalRevenue / 100000.0).formatOneDecimal()}L",
                        trend = "+31.4%",
                        color = TealPrimary,
                        icon = Icons.Default.AttachMoney
                    )
                    MetricCard(
                        modifier = Modifier.weight(1f),
                        title = "Pipeline Value",
                        value = "₹${(pipelineValue / 100000.0).formatOneDecimal()}L",
                        trend = "+12.0%",
                        color = StageProposal,
                        icon = Icons.Default.PieChart
                    )
                }
            }
        }

        // Quick Actions Bar
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Quick Actions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        QuickActionButton("Add Lead", Icons.Default.PersonAdd, TealPrimary, onAddLeadClick)
                        QuickActionButton("Pipeline", Icons.Default.ViewKanban, StageNew, onNavigateToPipeline)
                        QuickActionButton("All Leads", Icons.Default.List, StageProposal, onNavigateToLeads)
                        QuickActionButton("Automate", Icons.Default.AutoAwesome, StageNegotiation, onNavigateToAutomation)
                    }
                }
            }
        }

        // Sales Funnel Breakdown Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sales Pipeline Funnel", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                        TextButton(onClick = onNavigateToPipeline) {
                            Text("View Board", color = TealPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    LeadStage.values().forEach { stage ->
                        val count = leads.count { it.stage == stage }
                        val stageColor = when (stage) {
                            LeadStage.NEW -> StageNew
                            LeadStage.QUALIFIED -> StageQualified
                            LeadStage.PROPOSAL -> StageProposal
                            LeadStage.NEGOTIATION -> StageNegotiation
                            LeadStage.WON -> StageWon
                            LeadStage.LOST -> StageLost
                        }
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(stage.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                                Text("$count leads", fontSize = 12.sp, color = Slate500)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { if (leads.isEmpty()) 0f else count.toFloat() / leads.size },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = stageColor,
                                trackColor = Slate100
                            )
                        }
                    }
                }
            }
        }

        // Recent Activity Feed
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Activity Feed", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                Text("${activities.size} items", fontSize = 12.sp, color = Slate500)
            }
        }

        items(activities.take(5)) { act ->
            ActivityItemRow(act)
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    trend: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = Slate500, fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Slate900)
            Spacer(modifier = Modifier.height(2.dp))
            Text(trend, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StageWon)
        }
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(title, fontSize = 11.sp, color = Slate800, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ActivityItemRow(activity: ActivityEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val (icon, color) = when (activity.type) {
                ActivityType.CALL -> Icons.Default.Call to StageNew
                ActivityType.MEETING -> Icons.Default.Groups to StageQualified
                ActivityType.EMAIL -> Icons.Default.Mail to StageProposal
                ActivityType.STAGE_CHANGE -> Icons.Default.TrendingUp to StageWon
                else -> Icons.Default.EventNote to Slate500
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(activity.title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
                Text(activity.description, fontSize = 11.sp, color = Slate500)
            }
            Text(activity.timeAgo, fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.Medium)
        }
    }
}

fun Double.formatOneDecimal(): String = String.format("%.1f", this)
