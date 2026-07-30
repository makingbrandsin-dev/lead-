package com.example.ui.screens.modals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.LeadEntity
import com.example.data.LeadStage
import com.example.ui.screens.tabs.formatOneDecimal
import com.example.ui.screens.tabs.getStageColor
import com.example.ui.theme.*

@Composable
fun LeadDetailModal(
    lead: LeadEntity,
    onDismiss: () -> Unit,
    onStageChange: (LeadStage) -> Unit,
    onDelete: () -> Unit
) {
    var isStageDropdownExpanded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Lead Detail View", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Slate900)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Profile Header Card
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(TealLightBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = lead.firstName.first().toString() + lead.lastName.firstOrNull()?.toString().orEmpty(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(lead.fullName, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Slate900)
                        Text("${lead.title} at ${lead.company}", fontSize = 13.sp, color = Slate500)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = Slate100)
                Spacer(modifier = Modifier.height(14.dp))

                // Quick Contact Actions Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ActionChip("Call", Icons.Default.Call, StageNew)
                    ActionChip("Email", Icons.Default.Email, StageProposal)
                    ActionChip("Schedule", Icons.Default.CalendarToday, StageQualified)
                    ActionChip("Note", Icons.Default.EditNote, TealPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Current Stage Selector
                Text("Pipeline Stage", fontSize = 12.sp, color = Slate500, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))

                Box {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = getStageColor(lead.stage).copy(alpha = 0.15f),
                        onClick = { isStageDropdownExpanded = true }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Stage: ${lead.stage.label}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = getStageColor(lead.stage)
                            )
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Change Stage",
                                tint = getStageColor(lead.stage)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = isStageDropdownExpanded,
                        onDismissRequest = { isStageDropdownExpanded = false }
                    ) {
                        LeadStage.values().forEach { stg ->
                            DropdownMenuItem(
                                text = { Text(stg.label, fontWeight = FontWeight.Medium) },
                                onClick = {
                                    onStageChange(stg)
                                    isStageDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Detailed Key Specs
                DetailRow("Email", lead.email)
                DetailRow("Phone", lead.phone.ifEmpty { "Not provided" })
                DetailRow("Deal Value", "₹${(lead.dealValue / 100000.0).formatOneDecimal()} Lakhs")
                DetailRow("Lead Score", "${lead.score}/100")
                DetailRow("Source", lead.source.label)
                DetailRow("Assigned Rep", lead.assignedTo)

                if (lead.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Notes & Strategy", fontSize = 12.sp, color = Slate500, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = Slate50
                    ) {
                        Text(
                            lead.notes,
                            fontSize = 13.sp,
                            color = Slate800,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete Lead")
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Text("Close", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = color, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 13.sp, color = Slate500)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate900)
    }
}
