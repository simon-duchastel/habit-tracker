package com.duchastel.simon.habittracker.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duchastel.simon.habittracker.repositories.IdentityRepository
import kotlinx.coroutines.launch

class SummaryPageViewModel(
    private val identityRepository: IdentityRepository,
): ViewModel() {

    var uiState: UIState by mutableStateOf(UIState(userId = null))

    init {
        viewModelScope.launch {
            val userId = identityRepository.userId()
            uiState = uiState.copy(userId = userId)
        }
    }

    data class UIState(
        val userId: String?
    )
}