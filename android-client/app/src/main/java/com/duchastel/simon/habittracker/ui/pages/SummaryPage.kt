package com.duchastel.simon.habittracker.ui.pages

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.duchastel.simon.habittracker.viewmodels.SummaryPageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SummaryPage(viewModel: SummaryPageViewModel = koinViewModel()) {
    val userId = viewModel.uiState.userId
    if (userId != null) {
        SummaryView(userId = userId)
    } else {
        CircularProgressIndicator()
    }
}

@Composable
fun SummaryView(userId: String) {
    Text("Your user id is $userId")
}