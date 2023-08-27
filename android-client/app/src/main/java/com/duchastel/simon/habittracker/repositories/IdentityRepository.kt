package com.duchastel.simon.habittracker.repositories

interface IdentityRepository {
    suspend fun userId(): String
}