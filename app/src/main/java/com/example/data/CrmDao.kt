package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CrmDao {
    @Query("SELECT * FROM leads ORDER BY createdAt DESC")
    fun getAllLeads(): Flow<List<LeadEntity>>

    @Query("SELECT * FROM leads WHERE id = :id")
    suspend fun getLeadById(id: Int): LeadEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: LeadEntity): Long

    @Update
    suspend fun updateLead(lead: LeadEntity)

    @Query("DELETE FROM leads WHERE id = :id")
    suspend fun deleteLeadById(id: Int)

    @Query("UPDATE leads SET stage = :newStage, lastActivity = :activity WHERE id = :id")
    suspend fun updateLeadStage(id: Int, newStage: LeadStage, activity: String)

    @Query("SELECT * FROM activities ORDER BY timestamp DESC LIMIT 30")
    fun getAllActivities(): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("SELECT * FROM automation_rules")
    fun getAllAutomationRules(): Flow<List<AutomationRuleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAutomationRule(rule: AutomationRuleEntity)

    @Update
    suspend fun updateAutomationRule(rule: AutomationRuleEntity)

    @Query("SELECT * FROM team_members")
    fun getAllTeamMembers(): Flow<List<TeamMemberEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamMembers(members: List<TeamMemberEntity>)
}
