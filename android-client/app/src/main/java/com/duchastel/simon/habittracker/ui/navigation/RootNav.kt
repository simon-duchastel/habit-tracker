package com.duchastel.simon.habittracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun RootNav() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = RootNavRoute.LoggedOut.route) {
        loggedOutNav(route = RootNavRoute.LoggedOut.route) {
            navController.navigate(RootNavRoute.LoggedIn.route)
        }
        loggedInNav(route = RootNavRoute.LoggedIn.route) {
            navController.popBackStack()
        }
    }
}

sealed class RootNavRoute(val route: String) {
    object LoggedIn: RootNavRoute("loggedIn")
    object LoggedOut: RootNavRoute("loggedOut")
}