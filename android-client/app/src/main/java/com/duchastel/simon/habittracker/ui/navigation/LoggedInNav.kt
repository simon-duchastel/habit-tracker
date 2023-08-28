package com.duchastel.simon.habittracker.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.duchastel.simon.habittracker.ui.pages.SummaryPage

fun NavGraphBuilder.loggedInNav(route: String, onLoggedOut: () -> Unit) {
    navigation(startDestination = LoggedInNavRoute.Summary.route, route = route) {
        composable(LoggedInNavRoute.Summary.route) {
            Column {
                SummaryPage()
                Button(onClick = onLoggedOut) {
                    Text("Click me!")
                }
            }
        }
        composable(LoggedInNavRoute.ViewDay.route) { Text("TODO - View Day") }
        composable(LoggedInNavRoute.Setting.route) { Text("TODO - Setting") }
        composable(LoggedInNavRoute.EditGoal.route) { Text("TODO - Edit Goal") }
    }
}

sealed class LoggedInNavRoute(val route: String) {
    object Summary: LoggedInNavRoute("summary")
    object ViewDay: LoggedInNavRoute("viewDay")
    object Setting: LoggedInNavRoute("setting")
    object EditGoal: LoggedInNavRoute("editGoal")
}

