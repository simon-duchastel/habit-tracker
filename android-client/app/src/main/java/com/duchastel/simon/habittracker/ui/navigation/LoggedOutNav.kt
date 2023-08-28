package com.duchastel.simon.habittracker.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.loggedOutNav(route: String, onLoggedIn: () -> Unit) {
    navigation(startDestination = LoggedOutNavRoute.Auth.route, route = route) {
        composable(LoggedOutNavRoute.Auth.route) {
            Column {
                Text("TODO - Auth")
                Button(onClick = onLoggedIn) {
                    Text("Press me!")
                }
            }
        }
    }
}

sealed class LoggedOutNavRoute(val route: String) {
    object Auth: LoggedInNavRoute("auth")
}

