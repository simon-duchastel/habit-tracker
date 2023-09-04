package com.duchastel.simon.habittracker.network.targets

import com.duchastel.simon.habittracker.network.models.HabitsSummaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HabitsSummaryTarget {
    @GET("/v1/{userId}/habits/summary")
    suspend fun getHabitsSummary(
        @Path("userId") userId: String,
        @Query("startingFrom") startingFrom: String? = null,
        @Query("count") count: Int? = null,
    ): Response<HabitsSummaryResponse>
}