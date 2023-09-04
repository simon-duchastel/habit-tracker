package com.duchastel.simon.habittracker.network.models

import androidx.annotation.Keep

@Keep
data class HabitsSummaryResponse(
    val habits: List<HabitSummary>
)

@Keep
data class HabitSummary(
    val date: String,
    val completed: List<String>,
    val uncompleted: List<String>,
)