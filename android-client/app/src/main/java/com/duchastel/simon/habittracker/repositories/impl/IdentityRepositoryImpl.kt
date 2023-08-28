package com.duchastel.simon.habittracker.repositories.impl

import com.duchastel.simon.habittracker.network.targets.IdentityTarget
import com.duchastel.simon.habittracker.repositories.IdentityRepository

class IdentityRepositoryImpl(
    private val identityTarget: IdentityTarget,
): IdentityRepository {
    override suspend fun userId(): String {
        val response = identityTarget.whoAmI()
        val identityResponse = response.body()
        return if (response.isSuccessful && identityResponse != null) {
            identityResponse.identity.userId
        } else {
            throw Exception("TODO - handle this gracefully")
        }
    }
}