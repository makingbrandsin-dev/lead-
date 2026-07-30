package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deals")
data class DealEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val leadId: Int,
    val company: String,
    val amount: Double,
    val stage: LeadStage,
    val priority: LeadPriority = LeadPriority.MEDIUM,
    val probability: Int = 50,
    val expectedClose: String = "This Month",
    val createdAt: Long = System.currentTimeMillis()
)

enum class ActivityType {
    CALL, MEETING, EMAIL, NOTE, TASK, STAGE_CHANGE
}

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val leadId: Int = 0,
    val title: String,
    val description: String,
    val type: ActivityType = ActivityType.NOTE,
    val actorName: String = "John Doe",
    val timeAgo: String = "Just now",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "automation_rules")
data class AutomationRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val triggerType: String,
    val actionType: String,
    var isEnabled: Boolean = true,
    val iconName: String = "lightning"
)

@Entity(tableName = "team_members")
data class TeamMemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val role: String,
    val email: String,
    val dealsClosed: Int,
    val revenue: Double,
    val activeLeads: Int
)
