package com.duchastel.simon.habittracker.di

import com.duchastel.simon.habittracker.viewmodels.SummaryPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<SummaryPageViewModel> { SummaryPageViewModel(get()) }
}