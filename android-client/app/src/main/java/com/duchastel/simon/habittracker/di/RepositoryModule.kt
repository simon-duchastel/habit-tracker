package com.duchastel.simon.habittracker.di

import com.duchastel.simon.habittracker.repositories.HabitsRepository
import com.duchastel.simon.habittracker.repositories.IdentityRepository
import com.duchastel.simon.habittracker.repositories.impl.HabitsRepositoryImpl
import com.duchastel.simon.habittracker.repositories.impl.IdentityRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<IdentityRepository> { IdentityRepositoryImpl(get()) }
    single<HabitsRepository> { HabitsRepositoryImpl(get()) }
}