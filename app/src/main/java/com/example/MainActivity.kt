package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.MakingBrandsTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.CrmViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CrmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MakingBrandsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentScreen by viewModel.currentAppScreen.collectAsState()
                    val toastMessage by viewModel.toastMessage.collectAsState()

                    // Handle toast messages
                    LaunchedEffect(toastMessage) {
                        toastMessage?.let {
                            Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                            viewModel.clearToast()
                        }
                    }

                    Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
                        when (screen) {
                            AppScreen.SPLASH -> SplashScreen(
                                onSplashFinished = {
                                    viewModel.navigateTo(AppScreen.ONBOARDING)
                                }
                            )
                            AppScreen.ONBOARDING -> OnboardingCarouselScreen(
                                onSkipOrFinish = {
                                    viewModel.navigateTo(AppScreen.WELCOME_AUTH)
                                }
                            )
                            AppScreen.WELCOME_AUTH -> WelcomeHomeScreen(
                                onLoginClick = {
                                    viewModel.navigateTo(AppScreen.SIGN_IN)
                                },
                                onCreateAccountClick = {
                                    viewModel.navigateTo(AppScreen.SIGN_IN)
                                },
                                onGuestClick = {
                                    viewModel.signInAnonymously { success, _ ->
                                        if (success) viewModel.navigateTo(AppScreen.MAIN_APP)
                                    }
                                }
                            )
                            AppScreen.SIGN_IN -> SignInScreen(
                                onBackClick = {
                                    viewModel.navigateTo(AppScreen.WELCOME_AUTH)
                                },
                                onSignInSuccess = {
                                    viewModel.navigateTo(AppScreen.MAIN_APP)
                                },
                                onSignInWithEmail = { email, pass, onResult ->
                                    viewModel.signInWithEmail(email, pass, onResult)
                                },
                                onSignUpWithEmail = { email, pass, name, onResult ->
                                    viewModel.signUpWithEmail(email, pass, name, onResult)
                                },
                                onSignInAnonymous = { onResult ->
                                    viewModel.signInAnonymously(onResult)
                                }
                            )
                            AppScreen.MAIN_APP -> MainAppScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
