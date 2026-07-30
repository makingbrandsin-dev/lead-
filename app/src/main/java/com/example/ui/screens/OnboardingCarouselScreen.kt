package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.mascot.LionMascot
import com.example.ui.components.mascot.LionPose
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingSlideData(
    val titlePrefix: String,
    val titleHighlight: String,
    val description: String,
    val pose: LionPose
)

val onboardingSlides = listOf(
    OnboardingSlideData(
        titlePrefix = "Visualize ",
        titleHighlight = "Your Funnel.",
        description = "Get a clear, real-time view of your sales pipeline and track every opportunity.",
        pose = LionPose.POINTING
    ),
    OnboardingSlideData(
        titlePrefix = "Manage Leads ",
        titleHighlight = "Effortlessly.",
        description = "Capture, organize and nurture leads all in one place.",
        pose = LionPose.HOLDING_TABLET
    ),
    OnboardingSlideData(
        titlePrefix = "Track Progress ",
        titleHighlight = "in Real-Time.",
        description = "Stay updated with real-time insights and performance analytics.",
        pose = LionPose.MAGNIFYING_GLASS
    ),
    OnboardingSlideData(
        titlePrefix = "Automate & Save ",
        titleHighlight = "More Time.",
        description = "Automate repetitive tasks and focus on what truly matters.",
        pose = LionPose.POINTING
    ),
    OnboardingSlideData(
        titlePrefix = "Collaborate ",
        titleHighlight = "with Your Team.",
        description = "Work together, share updates and close deals faster.",
        pose = LionPose.THUMBS_UP
    ),
    OnboardingSlideData(
        titlePrefix = "Let's Grow ",
        titleHighlight = "Your Business!",
        description = "You're all set to supercharge your sales pipeline.",
        pose = LionPose.CELEBRATING
    )
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingCarouselScreen(
    onSkipOrFinish: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { onboardingSlides.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE6FFFA),
                        Color(0xFFF8FAFC),
                        Color.White
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top Bar with Skip Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage < onboardingSlides.size - 1) {
                    TextButton(onClick = onSkipOrFinish) {
                        Text(
                            text = "Skip",
                            color = Slate500,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Pager Body
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) { page ->
                val slide = onboardingSlides[page]
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Headline Text
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = slide.titlePrefix + slide.titleHighlight,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = TealDark,
                            textAlign = TextAlign.Center,
                            lineHeight = 34.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = slide.description,
                            fontSize = 14.sp,
                            color = Slate500,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }

                    // Slide Specific Graphic Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Mascot on Left
                            LionMascot(
                                pose = slide.pose,
                                modifier = Modifier.size(160.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Interactive Visual Card on Right matching prompt images
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(0.9f)
                                    .clip(RoundedCornerShape(20.dp)),
                                color = Color.White,
                                shadowElevation = 10.dp
                            ) {
                                Box(modifier = Modifier.padding(12.dp)) {
                                    when (page) {
                                        0 -> FunnelVisualCard()
                                        1 -> LeadListVisualCard()
                                        2 -> AnalyticsVisualCard()
                                        3 -> AutomationVisualCard()
                                        4 -> TeamActivityVisualCard()
                                        else -> CompletionVisualCard()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Dots & CTA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    repeat(onboardingSlides.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val width by animateFloatAsState(
                            targetValue = if (isSelected) 28f else 10f,
                            label = "dotWidth"
                        )
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(10.dp)
                                .width(width.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) TealPrimary else Slate300)
                        )
                    }
                }

                // CTA Button
                if (pagerState.currentPage == onboardingSlides.size - 1) {
                    Button(
                        onClick = onSkipOrFinish,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                    ) {
                        Text(
                            text = "Get Started",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Get Started",
                            tint = Color.White
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onSkipOrFinish,
                            shape = RoundedCornerShape(24.dp),
                            border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                        ) {
                            Text("Skip", color = Slate700)
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TealPrimary)
                        ) {
                            Text("Next", color = Color.White, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// Visual Card 1: Sales Funnel (New -> Qualified -> Proposal -> Won)
@Composable
private fun FunnelVisualCard() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
        Text("Sales Funnel", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate800)
        listOf(
            "New Leads" to (StageNew to 320),
            "Qualified" to (StageQualified to 245),
            "Proposal" to (StageProposal to 128),
            "Won" to (StageWon to 32)
        ).forEachIndexed { idx, (stage, data) ->
            val (color, count) = data
            val fillFraction = (4 - idx) / 4.0f
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .weight(fillFraction)
                        .height(24.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    color = color
                ) {
                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 6.dp)) {
                        Text(stage, fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("$count", fontSize = 10.sp, color = Slate500, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// Visual Card 2: Leads List Card
@Composable
private fun LeadListVisualCard() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Leads", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Slate800)
            Surface(
                shape = CircleShape,
                color = TealLightBg,
                modifier = Modifier.size(20.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("+", color = TealPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        listOf(
            "Sarah Johnson" to ("New Lead" to StageNew),
            "Rohan Mehta" to ("Contacted" to StageQualified),
            "Emily Davis" to ("Follow Up" to StageProposal),
            "Michael Brown" to ("Proposal Sent" to StageNegotiation)
        ).forEach { (name, info) ->
            val (status, color) = info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(TealLightBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(name.first().toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TealPrimary)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Slate800)
                        Text(status, fontSize = 8.sp, color = Slate500)
                    }
                }
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

// Visual Card 3: Dashboard Analytics Card
@Composable
private fun AnalyticsVisualCard() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Dashboard", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate800)
            Text("This Month ▾", fontSize = 9.sp, color = Slate500)
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            color = TealDark
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Total Deals", fontSize = 9.sp, color = Color.White.copy(alpha = 0.8f))
                Text("128", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("+12%", fontSize = 9.sp, color = TealAccent, fontWeight = FontWeight.Bold)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text("Won Deals", fontSize = 8.sp, color = Slate500)
                Text("45", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StageWon)
            }
            Column {
                Text("In Progress", fontSize = 8.sp, color = Slate500)
                Text("63", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StageQualified)
            }
        }
        Text("Pipeline: ₹24,80,000", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Slate800)
    }
}

// Visual Card 4: Automation Toggles Card
@Composable
private fun AutomationVisualCard() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
        Text("Automations", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate800)
        listOf(
            "Auto Follow-ups" to true,
            "Email Reminders" to true,
            "Task Assignments" to true,
            "Deal Updates" to true
        ).forEach { (title, enabled) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Slate50)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 9.sp, fontWeight = FontWeight.Medium, color = Slate800)
                Box(
                    modifier = Modifier
                        .size(height = 12.dp, width = 22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(TealPrimary)
                )
            }
        }
    }
}

// Visual Card 5: Team Activity Card
@Composable
private fun TeamActivityVisualCard() {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Team Activity", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Slate800)
        Spacer(modifier = Modifier.height(6.dp))
        listOf(
            "John" to "updated deal 2m ago",
            "Priya" to "added a note 15m ago",
            "Rahul" to "moved a deal 1h ago",
            "Anita" to "completed task 2h ago"
        ).forEach { (user, act) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(TealPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(user.first().toString(), fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text("$user $act", fontSize = 9.sp, color = Slate800, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// Visual Card 6: Completion Checkmark Card
@Composable
private fun CompletionVisualCard() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = TealPrimary,
            modifier = Modifier.size(54.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Ready",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Pipeline Ready!", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Slate800)
    }
}
