package com.example.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.ui.screens.modals.AddLeadModal
import com.example.ui.screens.modals.LeadDetailModal
import com.example.ui.screens.tabs.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.BottomTab
import com.example.ui.viewmodel.CrmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: CrmViewModel
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val leads by viewModel.filteredLeads.collectAsState()
    val allLeads by viewModel.allLeads.collectAsState()
    val activities by viewModel.allActivities.collectAsState()
    val rules by viewModel.allAutomationRules.collectAsState()
    val teamMembers by viewModel.allTeamMembers.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedStageFilter by viewModel.selectedStageFilter.collectAsState()
    val selectedLead by viewModel.selectedLead.collectAsState()
    val isAddLeadOpen by viewModel.isAddLeadOpen.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showUserMenu by remember { mutableStateOf(false) }

    val userInitials = remember(currentUser) {
        val name = currentUser?.displayName
        val email = currentUser?.email
        when {
            !name.isNullOrBlank() -> name.split(" ").mapNotNull { it.firstOrNull()?.uppercaseChar() }.take(2).joinToString("")
            !email.isNullOrBlank() -> email.take(2).uppercase()
            currentUser?.isAnonymous == true -> "GU"
            else -> "MB"
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Brand Logo Title
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TealPrimary,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("MB", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("making brands", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Slate900)
                            Text("Smart CRM Suite", fontSize = 10.sp, color = TealPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Right Notifications + User Avatar
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { showNotificationsDialog = true }) {
                            BadgedBox(
                                badge = {
                                    Badge(containerColor = StageNegotiation) {
                                        Text("${activities.size}")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.NotificationsNone, contentDescription = "Notifications", tint = Slate700)
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Box {
                            IconButton(
                                onClick = { showUserMenu = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(TealDark)
                            ) {
                                Text(userInitials, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            DropdownMenu(
                                expanded = showUserMenu,
                                onDismissRequest = { showUserMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = currentUser?.displayName ?: if (currentUser?.isAnonymous == true) "Guest User" else "CRM User",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = currentUser?.email ?: if (currentUser?.isAnonymous == true) "Anonymous Session" else "Authenticated User",
                                                fontSize = 11.sp,
                                                color = Slate500
                                            )
                                        }
                                    },
                                    onClick = { },
                                    enabled = false
                                )
                                Divider()
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Logout,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("Sign Out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                                        }
                                    },
                                    onClick = {
                                        showUserMenu = false
                                        viewModel.signOut()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                BottomTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    val (icon, activeIcon) = when (tab) {
                        BottomTab.DASHBOARD -> Icons.Default.Dashboard to Icons.Default.Dashboard
                        BottomTab.LEADS -> Icons.Default.PeopleOutline to Icons.Default.People
                        BottomTab.PIPELINE -> Icons.Default.ViewKanban to Icons.Default.ViewKanban
                        BottomTab.AUTOMATION -> Icons.Default.AutoAwesome to Icons.Default.AutoAwesome
                        BottomTab.ANALYTICS_TEAM -> Icons.Default.BarChart to Icons.Default.BarChart
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedTab(tab) },
                        icon = { Icon(if (isSelected) activeIcon else icon, contentDescription = tab.title) },
                        label = { Text(tab.title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = TealPrimary,
                            selectedTextColor = TealPrimary,
                            indicatorColor = TealLightBg,
                            unselectedIconColor = Slate500,
                            unselectedTextColor = Slate500
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Slate50)
        ) {
            Crossfade(targetState = selectedTab, label = "tabTransition") { tab ->
                when (tab) {
                    BottomTab.DASHBOARD -> DashboardTab(
                        leads = allLeads,
                        activities = activities,
                        onAddLeadClick = { viewModel.toggleAddLeadDialog(true) },
                        onLeadClick = { viewModel.selectLead(it) },
                        onNavigateToLeads = { viewModel.setSelectedTab(BottomTab.LEADS) },
                        onNavigateToPipeline = { viewModel.setSelectedTab(BottomTab.PIPELINE) },
                        onNavigateToAutomation = { viewModel.setSelectedTab(BottomTab.AUTOMATION) }
                    )
                    BottomTab.LEADS -> LeadsTab(
                        leads = leads,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        selectedStage = selectedStageFilter,
                        onStageSelect = { viewModel.setStageFilter(it) },
                        onLeadClick = { viewModel.selectLead(it) },
                        onAddLeadClick = { viewModel.toggleAddLeadDialog(true) }
                    )
                    BottomTab.PIPELINE -> PipelineTab(
                        leads = allLeads,
                        onMoveStage = { id, stage -> viewModel.updateLeadStage(id, stage) },
                        onLeadClick = { viewModel.selectLead(it) },
                        onAddLeadClick = { viewModel.toggleAddLeadDialog(true) }
                    )
                    BottomTab.AUTOMATION -> AutomationTab(
                        rules = rules,
                        onToggleRule = { viewModel.toggleAutomationRule(it) }
                    )
                    BottomTab.ANALYTICS_TEAM -> AnalyticsTeamTab(
                        leads = allLeads,
                        teamMembers = teamMembers,
                        onExportCsv = { viewModel.clearToast() }
                    )
                }
            }

            // Modals
            if (isAddLeadOpen) {
                AddLeadModal(
                    onDismiss = { viewModel.toggleAddLeadDialog(false) },
                    onSubmit = { fn, ln, em, ph, co, ti, dv, stg, src, nts ->
                        viewModel.addNewLead(fn, ln, em, ph, co, ti, dv, stg, src, nts)
                    }
                )
            }

            selectedLead?.let { lead ->
                LeadDetailModal(
                    lead = lead,
                    onDismiss = { viewModel.selectLead(null) },
                    onStageChange = { stg -> viewModel.updateLeadStage(lead.id, stg) },
                    onDelete = { viewModel.deleteLead(lead.id) }
                )
            }

            if (showNotificationsDialog) {
                AlertDialog(
                    onDismissRequest = { showNotificationsDialog = false },
                    title = { Text("Recent Notifications", fontWeight = FontWeight.Bold) },
                    text = {
                        Column {
                            activities.take(4).forEach { act ->
                                Text("• ${act.title} (${act.timeAgo})", fontSize = 13.sp, color = Slate800, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showNotificationsDialog = false }) {
                            Text("Dismiss", color = TealPrimary)
                        }
                    }
                )
            }
        }
    }
}
