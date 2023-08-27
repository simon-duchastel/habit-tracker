package com.duchastel.simon.habittracker.network.targets

import com.duchastel.simon.habittracker.network.models.IdentityResponse
import retrofit2.Response
import retrofit2.http.GET

interface IdentityTarget {
    @GET("/v1/whoami")
    suspend fun whoAmI(): Response<IdentityResponse>
}