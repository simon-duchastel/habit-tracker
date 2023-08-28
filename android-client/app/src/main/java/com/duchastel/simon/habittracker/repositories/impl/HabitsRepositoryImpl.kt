package com.duchastel.simon.habittracker.repositories.impl

import com.duchastel.simon.habittracker.network.models.HabitSummary
import com.duchastel.simon.habittracker.network.targets.HabitsSummaryTarget
import com.duchastel.simon.habittracker.repositories.HabitsRepository

class HabitsRepositoryImpl(
    private val habitsSummaryTarget: HabitsSummaryTarget,
): HabitsRepository {
    override suspend fun getSummary(userId: String): List<HabitSummary> {
        val response = habitsSummaryTarget.getHabitsSummary(userId)
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            return responseBody.habits
        } else {
            throw Exception("TODO - fix this")
        }
    }
}