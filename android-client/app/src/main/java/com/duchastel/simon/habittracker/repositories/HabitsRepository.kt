package com.duchastel.simon.habittracker.repositories

import com.duchastel.simon.habittracker.repositories.impl.UserId
import java.time.LocalDate

interface HabitsRepository {
    suspend fun getSummaryForDay(userId: UserId, date: LocalDate): HabitDaySummary?
}

data class HabitDaySummary(
    val completed: List<GoalId>,
    val uncompleted: List<GoalId>,
)

typealias GoalId = String