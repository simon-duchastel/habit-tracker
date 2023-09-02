package com.duchastel.simon.habittracker.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duchastel.simon.habittracker.repositories.HabitsRepository
import com.duchastel.simon.habittracker.repositories.IdentityRepository
import com.duchastel.simon.habittracker.repositories.impl.UserId
import com.duchastel.simon.habittracker.utils.asString
import com.duchastel.simon.habittracker.utils.firstDayInWeek
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.temporal.WeekFields

class SummaryPageViewModel(
    private val identityRepository: IdentityRepository,
    private val habitsRepository: HabitsRepository,
    private val weekFields: WeekFields,
): ViewModel() {

    var uiState: UIState by mutableStateOf(UIState(isLoading = true, habits = emptyList()))

    init {
        viewModelScope.launch {
            val currentDate = LocalDate.now()
            loadHabits(from = currentDate, 20)
        }
    }

    private suspend fun loadHabits(from: LocalDate, numWeeks: Int) {
        uiState = uiState.copy(isLoading = true)

        val userId = identityRepository.userId()

        var currentMonth = from.month
        val firstMonthLabel = HabitListElements.MonthLabel(currentMonth.asString())
        val restOfStatuses = (0L until numWeeks).flatMap {
            val day = from.minusWeeks(it)
            val firstDayInWeek = day.firstDayInWeek(weekFields)

            if (firstDayInWeek.month != currentMonth) {
                val firstWeekList = loadWeek(
                    day,
                    currentMonth,
                    userId,
                )
                currentMonth = firstDayInWeek.month
                val secondWeekList = loadWeek(
                    day,
                    currentMonth,
                    userId,
                )

                listOf(
                    HabitListElements.Week(firstWeekList),
                    HabitListElements.MonthLabel(currentMonth.asString()),
                    HabitListElements.Week(secondWeekList),
                )
            } else {
                val weekList = loadWeek(
                    day,
                    currentMonth,
                    userId,
                )
                listOf(HabitListElements.Week(weekList))
            }
        }

        val fullList = listOf(firstMonthLabel) + restOfStatuses
        uiState = uiState.copy(isLoading = false, habits = fullList)
    }

    private suspend fun loadWeek(
        from: LocalDate,
        currentMonth: Month,
        userId: UserId,
    ): List<HabitStatus> {
        val startOfWeek = from.firstDayInWeek(weekFields)
println("TODO START OF WEEK: $startOfWeek (${startOfWeek.dayOfWeek})")
        return (0L..6L).map {
            val day = startOfWeek.plusDays(it)
println("\tTODO Day        : $day (${day.dayOfWeek})")
            if (day.month != currentMonth) {
                HabitStatus.Absent
            } else {
                val summary = habitsRepository.getSummaryForDay(userId, day) ?: return@map HabitStatus.Empty
                val ratio = summary.completed.size.toFloat() / (summary.completed.size + summary.uncompleted.size).toFloat()
                HabitStatus.Complete(day, ratio)
            }
        }
    }

    fun loadMoreHabits() {
        uiState = uiState.copy(isLoading = true)
    }

    data class UIState(
        val isLoading: Boolean,
        val habits: List<HabitListElements>,
    )

    sealed class HabitListElements {
        data class MonthLabel(val month: String): HabitListElements()
        data class Week(val habits: List<HabitStatus>): HabitListElements()
    }

    sealed class HabitStatus {
        object Absent: HabitStatus()
        object Empty: HabitStatus()
        data class Complete(val date: LocalDate, val percent: Float): HabitStatus()
        data class Incomplete(val date: LocalDate, val percent: Float): HabitStatus()
    }
}

//val stubData = listOf(
//    SummaryPageViewModel.HabitListElements.MonthLabel("August"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(LocalDate.parse("2023-08-31"), 1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.3f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.2f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//    )),
//    SummaryPageViewModel.HabitListElements.MonthLabel("July"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.3f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.2f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.MonthLabel("June"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.3f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.2f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//    )),
//    SummaryPageViewModel.HabitListElements.MonthLabel("May"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//    )),
//    SummaryPageViewModel.HabitListElements.MonthLabel("April"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.3f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.2f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//    )),
//    SummaryPageViewModel.HabitListElements.MonthLabel("March"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.3f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.2f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.3f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.2f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
//    )),
//    SummaryPageViewModel.HabitListElements.MonthLabel("February"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.3f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.2f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//        SummaryPageViewModel.HabitStatus.Empty,
//        SummaryPageViewModel.HabitStatus.Empty,
//    )),
//    SummaryPageViewModel.HabitListElements.MonthLabel("January"),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//    SummaryPageViewModel.HabitListElements.Week(listOf(
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(1.0f),
//        SummaryPageViewModel.HabitStatus.Complete(0.7f),
//        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
//        SummaryPageViewModel.HabitStatus.Complete(0.0f),
//        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
//    )),
//)

