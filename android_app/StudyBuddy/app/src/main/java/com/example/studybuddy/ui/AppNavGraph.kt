package com.example.studybuddy.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studybuddy.ui.screens.AddEditScreen
import com.example.studybuddy.ui.screens.AuthScreen
import com.example.studybuddy.ui.screens.ListScreen
import com.example.studybuddy.viewmodel.AssignmentViewModel
import com.example.studybuddy.viewmodel.AuthViewModel


object Routes {
    const val AUTH       = "auth"
    const val LIST       = "list"
    const val ADD        = "add"
    const val EDIT       = "edit/{assignmentId}"

    fun edit(id: Int) = "edit/$id"
}


@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    assignmentViewModel: AssignmentViewModel,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {

        // screen 1 - logic and register
        composable(Routes.AUTH) {
            AuthScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    val userId = authViewModel.getLoggedInUserId()
                    assignmentViewModel.setUser(userId)
                    navController.navigate(Routes.LIST) {
                        // Remove the auth screen from the back stack so pressing
                        // back on the list screen doesn't return to login
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }

        // screen 2 - assignment list
        composable(Routes.LIST) {
            ListScreen(
                viewModel = assignmentViewModel,
                onAddClick = { navController.navigate(Routes.ADD) },
                onEditClick = { id -> navController.navigate(Routes.edit(id)) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // screen 3 - add a new assignment
        composable(Routes.ADD) {
            AddEditScreen(
                viewModel = assignmentViewModel,
                assignmentId = null,
                onDone = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // screen 3 - edit existing assignment
        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument("assignmentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("assignmentId")
            AddEditScreen(
                viewModel = assignmentViewModel,
                assignmentId = id,
                onDone = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
