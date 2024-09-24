package com.dicoding.biometricauth

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.biometricauth.home.HomeScreen
import com.dicoding.biometricauth.navigation.Screen
import com.dicoding.biometricauth.welcome.WelcomeScreen

@Composable
fun BiometricApp (
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            WelcomeScreen(navigateToHome = {
                navController.navigate(Screen.Home.route)
            })
        }

        composable(Screen.Home.route) {
            HomeScreen()
        }
    }
}