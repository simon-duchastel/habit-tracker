package com.duchastel.simon.habittracker.di

import com.duchastel.simon.habittracker.network.targets.IdentityTarget
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.scope.Scope
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    // Network infrastructure
    single<OkHttpClient> {
        OkHttpClient()
            .newBuilder()
            .addInterceptor {
                val request: Request = it.request()
                val newRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer $TEST_TOKEN_DO_NOT_COMMIT")
                    .build()
                it.proceed(newRequest)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .client(get())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Targets
    single { buildNetworkTarget(target = IdentityTarget::class.java) }
}

// Helper function to automatically resolve retrofit and create network targets a bit more easily
fun <T> Scope.buildNetworkTarget(retrofit: Retrofit = get(), target: Class<T>): T = retrofit.create(target)

const val BASE_URL = "https://habit-tracker-5bp5c3riyq-uw.a.run.app/"
const val TEST_TOKEN_DO_NOT_COMMIT = ""