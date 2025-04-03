package com.company.timetable.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.company.timetable.ui.auth.AuthState
import com.company.timetable.ui.auth.AuthViewModel
import com.company.timetable.ui.auth.LoginScreen
import com.company.timetable.ui.auth.RegisterScreen
import com.company.timetable.ui.home.HomeScreen
import com.company.timetable.ui.timetable.TimetableProcessingScreen
import com.company.timetable.ui.timetable.TimetableResultScreen
import com.company.timetable.ui.timetable.TimetableState
import com.company.timetable.ui.timetable.TimetableViewModel
import com.company.timetable.ui.timetable.UploadTimetableScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object UploadTimetable : Screen("upload_timetable")
    object ProcessingTimetable : Screen("processing_timetable")
    object TimetableResult : Screen("timetable_result")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel = hiltViewModel(),
    timetableViewModel: TimetableViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val timetableState by timetableViewModel.timetableState.collectAsState()
    
    // When user is authenticated, check if they have a timetable registered
    if (authState is AuthState.Authenticated) {
        val user = (authState as AuthState.Authenticated).user
        LaunchedEffect(user.uid) {
            timetableViewModel.checkTimetableRegistration(user.uid)
        }
    }
    
    // Navigate based on timetable state changes
    LaunchedEffect(timetableState) {
        if (authState is AuthState.Authenticated) {
            when (timetableState) {
                TimetableState.NotRegistered -> {
                    if (navController.currentDestination?.route != Screen.UploadTimetable.route) {
                        navController.navigate(Screen.UploadTimetable.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
                TimetableState.Registered -> {
                    if (navController.currentDestination?.route != Screen.Home.route) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                }
                TimetableState.Processing -> {
                    if (navController.currentDestination?.route != Screen.ProcessingTimetable.route) {
                        navController.navigate(Screen.ProcessingTimetable.route) {
                            popUpTo(Screen.UploadTimetable.route) { inclusive = true }
                        }
                    }
                }
                is TimetableState.Success -> {
                    if (navController.currentDestination?.route != Screen.TimetableResult.route) {
                        navController.navigate(Screen.TimetableResult.route) {
                            popUpTo(Screen.ProcessingTimetable.route) { inclusive = true }
                        }
                    }
                }
                is TimetableState.Error -> {
                    // Navigate back to upload screen on error
                    if (navController.currentDestination?.route != Screen.UploadTimetable.route) {
                        navController.navigate(Screen.UploadTimetable.route) {
                            popUpTo(Screen.ProcessingTimetable.route) { inclusive = true }
                        }
                    }
                }
                else -> {}
            }
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = when (authState) {
            is AuthState.Authenticated -> Screen.Home.route
            else -> Screen.Login.route
        }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLogin = { email, password ->
                    authViewModel.login(email, password)
                },
                onRegisterClick = {
                    navController.navigate(Screen.Register.route)
                },
                authViewModel = authViewModel
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegister = { email, password ->
                    authViewModel.register(email, password)
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    authViewModel.logout()
                    // Navigate to login screen on logout
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.UploadTimetable.route) {
            UploadTimetableScreen(
                onImageSelected = {
                    // Handle image selected event
                },
                onUploadClicked = {
                    // Navigation is handled by LaunchedEffect
                }
            )
        }
        
        composable(Screen.ProcessingTimetable.route) {
            TimetableProcessingScreen()
        }
        
        composable(Screen.TimetableResult.route) {
            TimetableResultScreen(
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
    }
} 