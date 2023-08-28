package com.duchastel.simon.habittracker.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duchastel.simon.habittracker.network.models.HabitSummary
import com.duchastel.simon.habittracker.repositories.HabitsRepository
import com.duchastel.simon.habittracker.repositories.IdentityRepository
import kotlinx.coroutines.launch

class SummaryPageViewModel(
    private val identityRepository: IdentityRepository,
    private val habitsRepository: HabitsRepository,
): ViewModel() {

    var uiState: UIState by mutableStateOf(UIState(isLoading = true, habits = emptyList()))

    init {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            val userId = identityRepository.userId()
            val habitsSummary = habitsRepository.getSummary(userId)

            uiState = uiState.copy(isLoading = false, habits = habitsSummary)
        }
    }

    data class UIState(
        val isLoading: Boolean,
        val habits: List<HabitSummary>,
    )
}