package com.duchastel.simon.habittracker.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        repositoryModule,
        viewModelModule,
        utilsModule,
    )
}