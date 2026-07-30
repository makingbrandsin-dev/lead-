package com.example.ui.screens.tabs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AutomationRuleEntity
import com.example.ui.theme.*

@Composable
fun AutomationTab(
    rules: List<AutomationRuleEntity>,
    onToggleRule: (AutomationRuleEntity) -> Unit
) {
    var isAddRuleDialogOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 90.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Banner Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = TealDark)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Smart CRM Workflows",
                            fontSize = 12.sp,
                            color = TealAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = TealAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Automate & Save More Time",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        "Automate repetitive follow-ups, deal alerts, and task routing automatically.",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Header Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Active Automation Rules (${rules.count { it.isEnabled }}/${rules.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate900
                )
                Button(
                    onClick = { isAddRuleDialogOpen = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Rule", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Rule", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // List of Rules
        items(rules, key = { it.id }) { rule ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (rule.isEnabled) TealLightBg else Slate100,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ElectricBolt,
                                    contentDescription = null,
                                    tint = if (rule.isEnabled) TealPrimary else Slate500
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(rule.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text(rule.description, fontSize = 12.sp, color = Slate500)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Slate100
                                ) {
                                    Text(
                                        "Trigger: ${rule.triggerType}",
                                        fontSize = 10.sp,
                                        color = Slate700,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Slate100
                                ) {
                                    Text(
                                        "Action: ${rule.actionType}",
                                        fontSize = 10.sp,
                                        color = Slate700,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Switch(
                        checked = rule.isEnabled,
                        onCheckedChange = { onToggleRule(rule) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = TealPrimary
                        )
                    )
                }
            }
        }
    }

    if (isAddRuleDialogOpen) {
        AlertDialog(
            onDismissRequest = { isAddRuleDialogOpen = false },
            title = { Text("Create Automation Rule", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Select a trigger event and corresponding automated action to streamline your pipeline.", fontSize = 13.sp, color = Slate500)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("• When Lead Created -> Send Follow-up Email", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Text("• When Inactive 3 Days -> Notify Sales Rep", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Text("• When Deal Won -> Post to Team Feed", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                }
            },
            confirmButton = {
                Button(
                    onClick = { isAddRuleDialogOpen = false },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                ) {
                    Text("Activate Rule")
                }
            },
            dismissButton = {
                TextButton(onClick = { isAddRuleDialogOpen = false }) {
                    Text("Cancel", color = Slate700)
                }
            }
        )
    }
}
