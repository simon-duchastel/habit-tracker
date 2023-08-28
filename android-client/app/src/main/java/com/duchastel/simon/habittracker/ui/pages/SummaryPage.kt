package com.duchastel.simon.habittracker.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.duchastel.simon.habittracker.network.models.HabitSummary
import com.duchastel.simon.habittracker.ui.theme.HabitSummaryCompleteStrong
import com.duchastel.simon.habittracker.ui.theme.HabitSummaryCompleteWeak
import com.duchastel.simon.habittracker.ui.theme.HabitSummaryIncompleteStrong
import com.duchastel.simon.habittracker.ui.theme.HabitSummaryIncompleteWeak
import com.duchastel.simon.habittracker.utils.blendColors
import com.duchastel.simon.habittracker.viewmodels.SummaryPageViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SummaryPage(viewModel: SummaryPageViewModel = koinViewModel()) {
    val uiState = viewModel.uiState
    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        SummaryView(uiState.habits)
    }
}

@Composable
fun SummaryView(habits: List<HabitSummary>) {
    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    ) {
        Row(modifier = Modifier.fillMaxWidth(0.90f),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            repeat(7) { i ->
                val size = (LocalConfiguration.current.screenWidthDp / 8).dp

                val iPrime = i + 1
                HabitSummaryBox(size = size, completed = iPrime, uncompleted = 7 - iPrime)
            }
        }
    }
}

@Composable
fun HabitSummaryBox(size: Dp = 40.dp, completed: Int, uncompleted: Int) {
    val total = completed + uncompleted
    val midpoint = total / 2f

    val color = if (completed >= midpoint) {
        // the further above the midpoint you get (closer to total), the stronger the complete color
        val percentage = (completed - midpoint) / midpoint
        blendColors(HabitSummaryCompleteWeak, HabitSummaryCompleteStrong, percentage)
    } else {
        // the further below the midpoint you get (closer to 0), the stronger the incomplete color
        val percentage = completed / midpoint
        blendColors(HabitSummaryIncompleteStrong, HabitSummaryIncompleteWeak, percentage)
    }

    Box(modifier = Modifier
        .size(size)
        .padding(4.dp)
        .clip(RectangleShape)
        .background(color)
    )
}