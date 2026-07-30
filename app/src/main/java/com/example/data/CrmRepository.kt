package com.example.data

import kotlinx.coroutines.flow.Flow

class CrmRepository(private val crmDao: CrmDao) {
    val allLeads: Flow<List<LeadEntity>> = crmDao.getAllLeads()
    val allActivities: Flow<List<ActivityEntity>> = crmDao.getAllActivities()
    val allAutomationRules: Flow<List<AutomationRuleEntity>> = crmDao.getAllAutomationRules()
    val allTeamMembers: Flow<List<TeamMemberEntity>> = crmDao.getAllTeamMembers()

    suspend fun insertLead(lead: LeadEntity): Long = crmDao.insertLead(lead)
    suspend fun updateLead(lead: LeadEntity) = crmDao.updateLead(lead)
    suspend fun deleteLead(id: Int) = crmDao.deleteLeadById(id)
    suspend fun updateLeadStage(id: Int, newStage: LeadStage, activityDescription: String) {
        crmDao.updateLeadStage(id, newStage, activityDescription)
        crmDao.insertActivity(
            ActivityEntity(
                leadId = id,
                title = "Stage updated to ${newStage.label}",
                description = activityDescription,
                type = ActivityType.STAGE_CHANGE,
                actorName = "Current User",
                timeAgo = "Just now"
            )
        )
    }

    suspend fun addActivity(activity: ActivityEntity) = crmDao.insertActivity(activity)
    suspend fun updateAutomationRule(rule: AutomationRuleEntity) = crmDao.updateAutomationRule(rule)
}
