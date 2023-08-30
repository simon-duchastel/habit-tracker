package com.duchastel.simon.habittracker.repositories.impl

import com.duchastel.simon.habittracker.network.targets.HabitsSummaryTarget
import com.duchastel.simon.habittracker.repositories.HabitDaySummary
import com.duchastel.simon.habittracker.repositories.HabitsRepository
import com.duchastel.simon.habittracker.utils.parseAsLocalDate
import java.time.LocalDate

class HabitsRepositoryImpl(
    private val habitsSummaryTarget: HabitsSummaryTarget,
): HabitsRepository {
    private val caches = mutableMapOf<UserId, Caches>()
    private fun getCaches(userId: UserId): Caches {
        return caches[userId] ?: Caches(mutableMapOf()).also { caches[userId] = it  }
    }

    override suspend fun getSummaryForDay(userId: UserId, date: LocalDate): HabitDaySummary? {
        val cache = getCaches(userId).habitsByDay
        val cachedValue = cache[date]
        if (cachedValue != null) return cachedValue

        fetchAndUpdateCache(userId, date)
        return cache[date]
    }

    private suspend fun fetchAndUpdateCache(userId: UserId, date: LocalDate): HabitDaySummary? {
        val cache = getCaches(userId).habitsByDay
        val response = habitsSummaryTarget.getHabitsSummary(userId)
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            responseBody.habits.forEach {
                val parsedDate = it.date.parseAsLocalDate() ?: throw Exception("TODO - fix this")
                cache[parsedDate] = HabitDaySummary(completed = it.completed, uncompleted = it.uncompleted)
            }
        } else {
            throw Exception("TODO - fix this")
        }

        return cache[date]
    }

    data class Caches(
        val habitsByDay: HabitsByDayCache
    )
}

typealias HabitsByDayCache = MutableMap<LocalDate, HabitDaySummary>