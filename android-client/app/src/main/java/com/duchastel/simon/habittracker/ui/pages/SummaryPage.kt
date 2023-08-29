package com.duchastel.simon.habittracker.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.duchastel.simon.habittracker.ui.theme.HabitColor
import com.duchastel.simon.habittracker.ui.theme.Typography
import com.duchastel.simon.habittracker.utils.blendColors
import com.duchastel.simon.habittracker.viewmodels.SummaryPageViewModel
import com.duchastel.simon.habittracker.viewmodels.SummaryPageViewModel.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun SummaryPage(viewModel: SummaryPageViewModel = koinViewModel()) {
    val uiState = viewModel.uiState
    if (uiState.isLoading && uiState.habits.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        SummaryView(uiState.isLoading, uiState.habits) { viewModel.loadMoreHabits() }
    }
}

@Composable
fun SummaryView(isLoading: Boolean, habits: List<HabitListElements>, onLoadMore: () -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
    ) {
        items(habits) { habitElement ->
            when (habitElement) {
                is HabitListElements.MonthLabel -> MonthLabel(text = habitElement.month)
                is HabitListElements.Week -> HabitSummaryRow(habits = habitElement.habits)
            }
        }
        item {
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.padding(16.dp))
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    LoadMoreButton(onLoadMore)
                }
            }
        }
    }
}

@Composable
fun LoadMoreButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text("Load more")
    }
}

@Composable
fun MonthLabel(text: String) {
    Spacer(modifier = Modifier.padding(8.dp))
    Text(text, style = Typography.titleLarge)
    Spacer(modifier = Modifier.padding(8.dp))
}

@Composable
fun HabitSummaryRow(habits: List<HabitStatus>) {
    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val size = (LocalConfiguration.current.screenWidthDp / 8).dp
        habits.forEach { habit ->
            when (habit) {
                is HabitStatus.Empty -> EmptyHabitSummaryBox(size = size)
                is HabitStatus.Complete -> HabitSummaryBox(
                    completed = true,
                    percentage = habit.percent,
                    size = size) { }
                is HabitStatus.Incomplete -> HabitSummaryBox(
                    completed = false,
                    percentage = habit.percent,
                    size = size) { }
            }
        }
    }
}

@Composable
fun HabitSummaryBox(
    completed: Boolean,
    percentage: Float,
    size: Dp = 40.dp,
    onClick: () -> Unit,
) {
    val color = if (completed) {
        blendColors(
            HabitColor.HabitSummaryCompleteWeak,
            HabitColor.HabitSummaryCompleteStrong,
            percentage
        )
    } else {
        blendColors(
            HabitColor.HabitSummaryIncompleteStrong,
            HabitColor.HabitSummaryIncompleteWeak,
            percentage
        )
    }

    Box(modifier = Modifier
        .size(size)
        .padding(4.dp)
        .clip(RectangleShape)
        .background(color)
        .clickable(onClick = onClick)
    )
}

@Composable
fun EmptyHabitSummaryBox(size: Dp = 40.dp) {
    Box(modifier = Modifier
        .size(size)
        .padding(4.dp)
        .clip(RectangleShape)
        .background(HabitColor.HabitSummaryEmpty)
    )
}