package com.example.spike.Navigatione

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.example.spike.ui.screen.HomeScreen
import com.example.spike.ui.screen.SearchScreen
import com.example.spike.ui.screen.LibraryScreen
import com.example.spike.ui.screen.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigator(navController: NavHostController) {
    AnimatedNavHost(
        navController = navController,
        startDestination = "home",
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
    ) {
        composable("home") { HomeScreen(navController) }
        composable("search") { SearchScreen() }
        composable("library") { LibraryScreen() }
        composable("settings") { SettingsScreen(navController) }
    }
}
