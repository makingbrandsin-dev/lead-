package com.example.ui.screens.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
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
import com.example.data.LeadStage
import com.example.ui.theme.*

@Composable
fun PipelineTab(
    leads: List<LeadEntity>,
    onMoveStage: (Int, LeadStage) -> Unit,
    onLeadClick: (LeadEntity) -> Unit,
    onAddLeadClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Deals Kanban Board", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Slate900)
                Text("Swipe horizontally to view pipeline stages", fontSize = 12.sp, color = Slate500)
            }
            IconButton(
                onClick = onAddLeadClick,
                colors = IconButtonDefaults.iconButtonColors(containerColor = TealLightBg)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Lead", tint = TealPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Kanban Columns
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(LeadStage.values()) { stage ->
                val stageLeads = leads.filter { it.stage == stage }
                val stageTotalValue = stageLeads.sumOf { it.dealValue }

                KanbanColumn(
                    stage = stage,
                    leads = stageLeads,
                    totalValue = stageTotalValue,
                    onMoveStage = onMoveStage,
                    onLeadClick = onLeadClick
                )
            }
        }
    }
}

@Composable
private fun KanbanColumn(
    stage: LeadStage,
    leads: List<LeadEntity>,
    totalValue: Double,
    onMoveStage: (Int, LeadStage) -> Unit,
    onLeadClick: (LeadEntity) -> Unit
) {
    val color = getStageColor(stage)

    Surface(
        modifier = Modifier
            .width(270.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(16.dp)),
        color = Slate100
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Column Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(stage.label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)
                }
                Surface(
                    shape = CircleShape,
                    color = Color.White
                ) {
                    Text(
                        "${leads.size}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Total: ₹${(totalValue / 100000.0).formatOneDecimal()} Lakhs",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate500
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Cards in Column
            if (leads.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No deals in ${stage.label}", fontSize = 12.sp, color = Slate500)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(leads, key = { it.id }) { lead ->
                        KanbanLeadCard(
                            lead = lead,
                            color = color,
                            onMoveStage = onMoveStage,
                            onLeadClick = onLeadClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KanbanLeadCard(
    lead: LeadEntity,
    color: Color,
    onMoveStage: (Int, LeadStage) -> Unit,
    onLeadClick: (LeadEntity) -> Unit
) {
    val currentStageOrdinal = lead.stage.ordinal

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLeadClick(lead) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(lead.fullName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Slate900)
            Text(lead.company, fontSize = 11.sp, color = Slate500)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "₹${(lead.dealValue / 100000.0).formatOneDecimal()}L",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text("Rep: ${lead.assignedTo.split(" ").first()}", fontSize = 10.sp, color = Slate500)
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Slate100)
            Spacer(modifier = Modifier.height(6.dp))

            // Stage Navigation Quick Actions (Prev / Next)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentStageOrdinal > 0) {
                    IconButton(
                        onClick = { onMoveStage(lead.id, LeadStage.values()[currentStageOrdinal - 1]) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Move Prev",
                            tint = Slate500,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(24.dp))
                }

                Text("Score: ${lead.score}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate700)

                if (currentStageOrdinal < LeadStage.values().size - 1) {
                    IconButton(
                        onClick = { onMoveStage(lead.id, LeadStage.values()[currentStageOrdinal + 1]) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Move Next",
                            tint = TealPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(24.dp))
                }
            }
        }
    }
}
