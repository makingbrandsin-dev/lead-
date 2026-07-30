package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        LeadEntity::class,
        DealEntity::class,
        ActivityEntity::class,
        AutomationRuleEntity::class,
        TeamMemberEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun crmDao(): CrmDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "making_brands_crm.db"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.crmDao())
                }
            }
        }

        suspend fun populateDatabase(dao: CrmDao) {
            // Seed sample leads matching prompt visuals
            val sampleLeads = listOf(
                LeadEntity(
                    firstName = "Sarah",
                    lastName = "Johnson",
                    email = "sarah.j@acmecorp.com",
                    phone = "+1 (555) 234-5678",
                    company = "Acme Corp",
                    title = "VP of Sales",
                    stage = LeadStage.NEW,
                    source = LeadSource.WEB_FORM,
                    dealValue = 450000.0,
                    score = 88,
                    priority = LeadPriority.HIGH,
                    assignedTo = "John Doe",
                    notes = "Interested in Enterprise CRM plan. Follow up this week."
                ),
                LeadEntity(
                    firstName = "Rohan",
                    lastName = "Mehta",
                    email = "rohan@techpulse.io",
                    phone = "+91 98765 43210",
                    company = "TechPulse India",
                    title = "Chief Technology Officer",
                    stage = LeadStage.QUALIFIED,
                    source = LeadSource.EMAIL,
                    dealValue = 820000.0,
                    score = 92,
                    priority = LeadPriority.HIGH,
                    assignedTo = "Priya Sharma",
                    notes = "Demo completed. Requested custom integrations."
                ),
                LeadEntity(
                    firstName = "Emily",
                    lastName = "Davis",
                    email = "emily.davis@globex.com",
                    phone = "+1 (555) 876-5432",
                    company = "Globex Solutions",
                    title = "Head of Operations",
                    stage = LeadStage.PROPOSAL,
                    source = LeadSource.ZAPIER,
                    dealValue = 340000.0,
                    score = 75,
                    priority = LeadPriority.MEDIUM,
                    assignedTo = "Rahul Verma",
                    notes = "Proposal sent for 50 seat license."
                ),
                LeadEntity(
                    firstName = "Michael",
                    lastName = "Brown",
                    email = "michael@innovate.co",
                    phone = "+1 (555) 345-6789",
                    company = "Innovate Labs",
                    title = "Marketing Director",
                    stage = LeadStage.NEGOTIATION,
                    source = LeadSource.REFERRAL,
                    dealValue = 560000.0,
                    score = 85,
                    priority = LeadPriority.HIGH,
                    assignedTo = "Anita Roy",
                    notes = "Finalizing terms & SLA agreement."
                ),
                LeadEntity(
                    firstName = "Vikram",
                    lastName = "Singh",
                    email = "vikram@apexbrand.in",
                    phone = "+91 91234 56789",
                    company = "Apex Brands",
                    title = "Founder & CEO",
                    stage = LeadStage.WON,
                    source = LeadSource.IMPORT,
                    dealValue = 1200000.0,
                    score = 98,
                    priority = LeadPriority.HIGH,
                    assignedTo = "Priya Sharma",
                    notes = "Closed 1 Year Contract! ₹12.00L ARR."
                ),
                LeadEntity(
                    firstName = "Jessica",
                    lastName = "Taylor",
                    email = "j.taylor@skyline.org",
                    phone = "+1 (555) 654-3210",
                    company = "Skyline Media",
                    title = "Growth Manager",
                    stage = LeadStage.QUALIFIED,
                    source = LeadSource.WEB_FORM,
                    dealValue = 280000.0,
                    score = 64,
                    priority = LeadPriority.MEDIUM,
                    assignedTo = "John Doe",
                    notes = "Looking for lead capture & email automation."
                )
            )

            sampleLeads.forEach { dao.insertLead(it) }

            // Seed activities matching prompt feed
            val sampleActivities = listOf(
                ActivityEntity(
                    title = "Follow-up call with John Doe",
                    description = "Discussed budget approval and timeline.",
                    type = ActivityType.CALL,
                    actorName = "John Doe",
                    timeAgo = "10:30 AM"
                ),
                ActivityEntity(
                    title = "Meeting with Acme Corp",
                    description = "Presented product demo to VP of Sales.",
                    type = ActivityType.MEETING,
                    actorName = "Priya Sharma",
                    timeAgo = "12:00 PM"
                ),
                ActivityEntity(
                    title = "Proposal sent to Globex",
                    description = "Sent custom quote for 50 user licenses.",
                    type = ActivityType.EMAIL,
                    actorName = "Rahul Verma",
                    timeAgo = "02:30 PM"
                ),
                ActivityEntity(
                    title = "Deal Moved to Won",
                    description = "Apex Brands contract signed successfully!",
                    type = ActivityType.STAGE_CHANGE,
                    actorName = "Anita Roy",
                    timeAgo = "2h ago"
                )
            )

            sampleActivities.forEach { dao.insertActivity(it) }

            // Seed automation rules matching prompt slide
            val sampleAutomation = listOf(
                AutomationRuleEntity(
                    title = "Auto Follow-ups",
                    description = "Send automated email 24h after new lead capture.",
                    triggerType = "Lead Created",
                    actionType = "Send Email",
                    isEnabled = true
                ),
                AutomationRuleEntity(
                    title = "Email Reminders",
                    description = "Notify sales rep if deal inactive for 3 days.",
                    triggerType = "Inactivity",
                    actionType = "Send Push Notification",
                    isEnabled = true
                ),
                AutomationRuleEntity(
                    title = "Task Assignments",
                    description = "Round-robin assignment for high-priority leads.",
                    triggerType = "Lead Score > 80",
                    actionType = "Assign Rep",
                    isEnabled = true
                ),
                AutomationRuleEntity(
                    title = "Deal Updates",
                    description = "Alert team channel when deal moves to Won stage.",
                    triggerType = "Stage Changed to Won",
                    actionType = "Post Activity",
                    isEnabled = true
                )
            )

            sampleAutomation.forEach { dao.insertAutomationRule(it) }

            // Seed team members
            val sampleTeam = listOf(
                TeamMemberEntity(
                    name = "Priya Sharma",
                    role = "Senior Account Exec",
                    email = "priya@makingbrands.in",
                    dealsClosed = 42,
                    revenue = 1450000.0,
                    activeLeads = 18
                ),
                TeamMemberEntity(
                    name = "John Doe",
                    role = "Sales Manager",
                    email = "john@makingbrands.in",
                    dealsClosed = 38,
                    revenue = 1280000.0,
                    activeLeads = 14
                ),
                TeamMemberEntity(
                    name = "Rahul Verma",
                    role = "Lead Gen Specialist",
                    email = "rahul@makingbrands.in",
                    dealsClosed = 29,
                    revenue = 940000.0,
                    activeLeads = 22
                ),
                TeamMemberEntity(
                    name = "Anita Roy",
                    role = "Customer Success",
                    email = "anita@makingbrands.in",
                    dealsClosed = 31,
                    revenue = 1050000.0,
                    activeLeads = 16
                )
            )

            dao.insertTeamMembers(sampleTeam)
        }
    }
}
