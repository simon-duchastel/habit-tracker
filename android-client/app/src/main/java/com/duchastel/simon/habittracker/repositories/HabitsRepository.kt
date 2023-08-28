package com.duchastel.simon.habittracker.repositories

import com.duchastel.simon.habittracker.network.models.HabitSummary

interface HabitsRepository {
    suspend fun getSummary(userId: String): List<HabitSummary>
}