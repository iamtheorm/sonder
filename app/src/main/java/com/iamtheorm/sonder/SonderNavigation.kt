package com.iamtheorm.sonder

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object SonderRoutes {
    const val WELCOME = "welcome"
    const val ROLE_SELECTION = "role_selection"
    const val PROFILE_SETUP = "profile_setup"
    const val OTP = "otp"
    const val HOME = "home"
}

@Composable
fun SonderNavigation(
    navController: NavHostController,
    viewModel: SonderViewModel,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(SonderRoutes.WELCOME) {
            WelcomeScreen(onNavigate = { navController.navigate(SonderRoutes.ROLE_SELECTION) })
        }
        composable(SonderRoutes.ROLE_SELECTION) {
            RoleSelectionScreen(
                viewModel = viewModel,
                onNavigate = { navController.navigate(SonderRoutes.PROFILE_SETUP) }
            )
        }
        composable(SonderRoutes.PROFILE_SETUP) {
            ProfileSetupScreen(
                viewModel = viewModel,
                onNavigate = { navController.navigate(SonderRoutes.OTP) }
            )
        }
        composable(SonderRoutes.OTP) {
            OtpScreen(
                viewModel = viewModel,
                onNavigate = {
                    navController.navigate(SonderRoutes.HOME) {
                        popUpTo(SonderRoutes.WELCOME) { inclusive = true }
                    }
                }
            )
        }
        composable(SonderRoutes.HOME) {
            HomeScreen(viewModel = viewModel)
        }
    }
}
