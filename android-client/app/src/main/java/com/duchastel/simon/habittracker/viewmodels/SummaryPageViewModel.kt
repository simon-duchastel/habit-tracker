package com.duchastel.simon.habittracker.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duchastel.simon.habittracker.repositories.HabitsRepository
import com.duchastel.simon.habittracker.repositories.IdentityRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class SummaryPageViewModel(
    private val identityRepository: IdentityRepository,
    private val habitsRepository: HabitsRepository,
): ViewModel() {

    var uiState: UIState by mutableStateOf(UIState(isLoading = true, habits = emptyList()))

    init {
        viewModelScope.launch {
            val currentDate = LocalDate.now()
            loadHabits(from = currentDate.minusDays(100), to = currentDate)
        }
    }

    private suspend fun loadHabits(from: LocalDate, to: LocalDate) {
        check(from.isBefore(to)) { "'from' date must be before 'to' date - $from is not before $to" }

        uiState = uiState.copy(isLoading = true)

        val userId = identityRepository.userId()
        val dates = mutableListOf(from)
        while (dates.last().isBefore(to)) {
            dates += dates.last().plusDays(1)
        }

        dates.map { habitsRepository.getSummaryForDay(userId, it) }

        uiState = uiState.copy(isLoading = false, habits = stubData)
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
        object Empty: HabitStatus()
        data class Complete(val percent: Float): HabitStatus()
        data class Incomplete(val percent: Float): HabitStatus()
    }
}

val stubData = listOf(
    SummaryPageViewModel.HabitListElements.MonthLabel("August"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.3f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.2f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
    )),
    SummaryPageViewModel.HabitListElements.MonthLabel("July"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.3f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.2f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.MonthLabel("June"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.3f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.2f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
    )),
    SummaryPageViewModel.HabitListElements.MonthLabel("May"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
    )),
    SummaryPageViewModel.HabitListElements.MonthLabel("April"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.3f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.2f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
    )),
    SummaryPageViewModel.HabitListElements.MonthLabel("March"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.3f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.2f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.3f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.2f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
    )),
    SummaryPageViewModel.HabitListElements.MonthLabel("February"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.3f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.2f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.6f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
        SummaryPageViewModel.HabitStatus.Empty,
        SummaryPageViewModel.HabitStatus.Empty,
    )),
    SummaryPageViewModel.HabitListElements.MonthLabel("January"),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
    SummaryPageViewModel.HabitListElements.Week(listOf(
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(1.0f),
        SummaryPageViewModel.HabitStatus.Complete(0.7f),
        SummaryPageViewModel.HabitStatus.Incomplete(0.2f),
        SummaryPageViewModel.HabitStatus.Complete(0.0f),
        SummaryPageViewModel.HabitStatus.Incomplete(1.0f),
    )),
)

