package com.example.skainet_android.auth.data.remote

import com.example.skainet_android.auth.data.TokenHolder
import com.example.skainet_android.auth.data.Admin
import com.example.skainet_android.core.Api
import com.example.skainet_android.core.Result
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object RemoteAuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body admin: Admin): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(admin: Admin): Result<TokenHolder> {
        try {
            return Result.Success(authService.login(admin))
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }
}

