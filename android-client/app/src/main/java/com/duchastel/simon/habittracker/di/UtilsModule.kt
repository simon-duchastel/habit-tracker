package com.duchastel.simon.habittracker.di

import org.koin.dsl.module
import java.time.temporal.WeekFields
import java.util.Locale

val utilsModule = module {
    single<WeekFields> { WeekFields.of(Locale.getDefault()) }
}