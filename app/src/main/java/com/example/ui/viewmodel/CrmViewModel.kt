package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH,
    ONBOARDING,
    WELCOME_AUTH,
    SIGN_IN,
    MAIN_APP
}

enum class BottomTab(val title: String) {
    DASHBOARD("Dashboard"),
    LEADS("Leads"),
    PIPELINE("Pipeline"),
    AUTOMATION("Automate"),
    ANALYTICS_TEAM("Analytics")
}

class CrmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CrmRepository

    private var firebaseAuth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        null
    }

    private val _currentUser = MutableStateFlow<FirebaseUser?>(firebaseAuth?.currentUser)
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()

    val allLeads: StateFlow<List<LeadEntity>>
    val allActivities: StateFlow<List<ActivityEntity>>
    val allAutomationRules: StateFlow<List<AutomationRuleEntity>>
    val allTeamMembers: StateFlow<List<TeamMemberEntity>>

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedStageFilter = MutableStateFlow<LeadStage?>(null)
    val selectedStageFilter: StateFlow<LeadStage?> = _selectedStageFilter.asStateFlow()

    private val _selectedLead = MutableStateFlow<LeadEntity?>(null)
    val selectedLead: StateFlow<LeadEntity?> = _selectedLead.asStateFlow()

    private val _isAddLeadOpen = MutableStateFlow(false)
    val isAddLeadOpen: StateFlow<Boolean> = _isAddLeadOpen.asStateFlow()

    private val _currentAppScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentAppScreen: StateFlow<AppScreen> = _currentAppScreen.asStateFlow()

    private val _onboardingSlideIndex = MutableStateFlow(0)
    val onboardingSlideIndex: StateFlow<Int> = _onboardingSlideIndex.asStateFlow()

    private val _selectedTab = MutableStateFlow(BottomTab.DASHBOARD)
    val selectedTab: StateFlow<BottomTab> = _selectedTab.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = CrmRepository(db.crmDao())

        allLeads = repository.allLeads.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allActivities = repository.allActivities.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allAutomationRules = repository.allAutomationRules.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        allTeamMembers = repository.allTeamMembers.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // Filtered Leads
    val filteredLeads: StateFlow<List<LeadEntity>> = combine(
        allLeads, searchQuery, selectedStageFilter
    ) { leads, query, stage ->
        leads.filter { lead ->
            val matchesQuery = query.isBlank() ||
                    lead.fullName.contains(query, ignoreCase = true) ||
                    lead.company.contains(query, ignoreCase = true) ||
                    lead.email.contains(query, ignoreCase = true)
            val matchesStage = stage == null || lead.stage == stage
            matchesQuery && matchesStage
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStageFilter(stage: LeadStage?) {
        _selectedStageFilter.value = stage
    }

    fun selectLead(lead: LeadEntity?) {
        _selectedLead.value = lead
    }

    fun toggleAddLeadDialog(open: Boolean) {
        _isAddLeadOpen.value = open
    }

    fun navigateTo(screen: AppScreen) {
        _currentAppScreen.value = screen
    }

    fun setOnboardingSlide(index: Int) {
        _onboardingSlideIndex.value = index.coerceIn(0, 5)
    }

    fun setSelectedTab(tab: BottomTab) {
        _selectedTab.value = tab
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun addNewLead(
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        company: String,
        title: String,
        dealValue: Double,
        stage: LeadStage,
        source: LeadSource,
        notes: String
    ) {
        viewModelScope.launch {
            val lead = LeadEntity(
                firstName = firstName,
                lastName = lastName,
                email = email,
                phone = phone,
                company = company,
                title = title,
                dealValue = dealValue,
                stage = stage,
                source = source,
                notes = notes,
                score = (50..95).random()
            )
            repository.insertLead(lead)
            _toastMessage.value = "Lead '$firstName $lastName' added successfully!"
            _isAddLeadOpen.value = false
        }
    }

    fun updateLeadStage(leadId: Int, newStage: LeadStage) {
        viewModelScope.launch {
            repository.updateLeadStage(leadId, newStage, "Moved stage to ${newStage.label}")
            _toastMessage.value = "Lead moved to ${newStage.label}"
        }
    }

    fun deleteLead(leadId: Int) {
        viewModelScope.launch {
            repository.deleteLead(leadId)
            _selectedLead.value = null
            _toastMessage.value = "Lead deleted"
        }
    }

    fun toggleAutomationRule(rule: AutomationRuleEntity) {
        viewModelScope.launch {
            val updated = rule.copy(isEnabled = !rule.isEnabled)
            repository.updateAutomationRule(updated)
            _toastMessage.value = "${rule.title} is now ${if (updated.isEnabled) "Enabled" else "Disabled"}"
        }
    }

    // Firebase Authentication Action Handlers
    fun signInWithEmail(email: String, pass: String, onResult: (Boolean, String?) -> Unit) {
        val auth = firebaseAuth
        if (auth == null) {
            _toastMessage.value = "Signed in as demo user ($email)"
            onResult(true, null)
            return
        }
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    _toastMessage.value = "Welcome back, ${auth.currentUser?.email ?: email}!"
                    onResult(true, null)
                } else {
                    val err = task.exception?.localizedMessage ?: "Sign-in failed."
                    onResult(false, err)
                }
            }
    }

    fun signUpWithEmail(email: String, pass: String, name: String, onResult: (Boolean, String?) -> Unit) {
        val auth = firebaseAuth
        if (auth == null) {
            _toastMessage.value = "Account created for $name!"
            onResult(true, null)
            return
        }
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user?.updateProfile(profileUpdates)
                    _currentUser.value = user
                    _toastMessage.value = "Account created for $name!"
                    onResult(true, null)
                } else {
                    val err = task.exception?.localizedMessage ?: "Registration failed."
                    onResult(false, err)
                }
            }
    }

    fun signInAnonymously(onResult: (Boolean, String?) -> Unit) {
        val auth = firebaseAuth
        if (auth == null) {
            _toastMessage.value = "Signed in as Guest"
            onResult(true, null)
            return
        }
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _currentUser.value = auth.currentUser
                    _toastMessage.value = "Signed in as Guest"
                    onResult(true, null)
                } else {
                    val err = task.exception?.localizedMessage ?: "Guest sign-in failed."
                    onResult(false, err)
                }
            }
    }

    fun signOut() {
        try {
            firebaseAuth?.signOut()
        } catch (e: Exception) {
            // Safe fallback
        }
        _currentUser.value = null
        _toastMessage.value = "Signed out"
        navigateTo(AppScreen.WELCOME_AUTH)
    }
}
