package com.duchastel.simon.habittracker.network.models

import androidx.annotation.Keep

@Keep
data class IdentityResponse(
    val identity: Identity
)

@Keep
data class Identity(
    val UserId: String
)