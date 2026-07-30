package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class LeadStage(val label: String) {
    NEW("New"),
    QUALIFIED("Qualified"),
    PROPOSAL("Proposal"),
    NEGOTIATION("Negotiation"),
    WON("Won"),
    LOST("Lost")
}

enum class LeadSource(val label: String) {
    WEB_FORM("Web Form"),
    EMAIL("Email"),
    IMPORT("CSV Import"),
    API("API Integration"),
    ZAPIER("Zapier"),
    REFERRAL("Referral")
}

enum class LeadPriority(val label: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

@Entity(tableName = "leads")
data class LeadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val company: String,
    val title: String,
    val stage: LeadStage = LeadStage.NEW,
    val source: LeadSource = LeadSource.WEB_FORM,
    val dealValue: Double = 0.0,
    val score: Int = 50,
    val priority: LeadPriority = LeadPriority.MEDIUM,
    val notes: String = "",
    val assignedTo: String = "Sales Team",
    val createdAt: Long = System.currentTimeMillis(),
    val lastActivity: String = "Lead Created"
) {
    val fullName: String get() = "$firstName $lastName"
}
