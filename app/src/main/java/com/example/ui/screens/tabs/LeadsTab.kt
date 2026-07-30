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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun LeadsTab(
    leads: List<LeadEntity>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedStage: LeadStage?,
    onStageSelect: (LeadStage?) -> Unit,
    onLeadClick: (LeadEntity) -> Unit,
    onAddLeadClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                placeholder = { Text("Search leads, companies or email...", fontSize = 13.sp, color = Slate500) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Slate500) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear", tint = Slate500)
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(26.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealPrimary,
                    unfocusedBorderColor = Slate300,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Horizontal Stage Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                item {
                    FilterChip(
                        selected = selectedStage == null,
                        onClick = { onStageSelect(null) },
                        label = { Text("All Leads (${leads.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TealPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
                items(LeadStage.values()) { stage ->
                    val isSelected = selectedStage == stage
                    val count = leads.count { it.stage == stage }
                    FilterChip(
                        selected = isSelected,
                        onClick = { onStageSelect(if (isSelected) null else stage) },
                        label = { Text("${stage.label} ($count)", fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = getStageColor(stage),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Leads List
            if (leads.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Slate300,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No leads found", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate800)
                        Text("Try adjusting your search or filters", fontSize = 13.sp, color = Slate500)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(leads, key = { it.id }) { lead ->
                        LeadCardItem(lead = lead, onClick = { onLeadClick(lead) })
                    }
                }
            }
        }

        // Floating Action Button (+)
        FloatingActionButton(
            onClick = onAddLeadClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 80.dp, end = 20.dp),
            containerColor = TealPrimary,
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Lead", modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun LeadCardItem(
    lead: LeadEntity,
    onClick: () -> Unit
) {
    val stageColor = getStageColor(lead.stage)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                // Lead Name + Avatar
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(TealLightBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lead.firstName.first().toString() + lead.lastName.firstOrNull()?.toString().orEmpty(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(lead.fullName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                        Text("${lead.title} • ${lead.company}", fontSize = 12.sp, color = Slate500)
                    }
                }

                // Stage Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = stageColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = lead.stage.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = stageColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Slate100)
            Spacer(modifier = Modifier.height(10.dp))

            // Deal Info & Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Deal Value", fontSize = 10.sp, color = Slate500)
                    Text(
                        "₹${(lead.dealValue / 100000.0).formatOneDecimal()} Lakhs",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Lead Score Ring
                    Surface(
                        shape = CircleShape,
                        color = Slate100
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Score",
                                tint = StageNegotiation,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${lead.score}/100",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Slate800
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Source Tag
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Slate100
                    ) {
                        Text(
                            lead.source.label,
                            fontSize = 10.sp,
                            color = Slate700,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

fun getStageColor(stage: LeadStage): Color = when (stage) {
    LeadStage.NEW -> StageNew
    LeadStage.QUALIFIED -> StageQualified
    LeadStage.PROPOSAL -> StageProposal
    LeadStage.NEGOTIATION -> StageNegotiation
    LeadStage.WON -> StageWon
    LeadStage.LOST -> StageLost
}
