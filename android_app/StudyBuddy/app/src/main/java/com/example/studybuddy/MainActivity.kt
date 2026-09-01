package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.ui.AppNavGraph
import com.example.studybuddy.ui.Routes
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import com.example.studybuddy.viewmodel.AssignmentViewModel
import com.example.studybuddy.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            StudyBuddyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {


                    val app = application as StudyBuddyApp

                    val authViewModel: AuthViewModel = viewModel(
                        factory = AuthViewModel.Factory(app.container.authRepository, app.container.sessionManager)
                    )
                    val assignmentViewModel: AssignmentViewModel = viewModel(
                        factory = AssignmentViewModel.Factory(app.container.assignmentRepository)
                    )


                    val startDestination = if (authViewModel.isLoggedIn()) {
                        assignmentViewModel.setUser(authViewModel.getLoggedInUserId())
                        Routes.LIST
                    } else {
                        Routes.AUTH
                    }

                    val navController = rememberNavController()

                    AppNavGraph(
                        navController        = navController,
                        authViewModel        = authViewModel,
                        assignmentViewModel  = assignmentViewModel,
                        startDestination     = startDestination
                    )
                }
            }
        }
    }
}
