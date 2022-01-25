package com.example.skainet_android.auth.data.remote

import com.example.skainet_android.auth.data.TokenHolder
import com.example.skainet_android.auth.data.Admin
import com.example.skainet_android.core.Api
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object AuthDataSource {
    interface AuthService {
        @Headers("Content-Type: application/json")
        @POST("/api/auth/login")
        suspend fun login(@Body admin: Admin): TokenHolder
    }

    private val authService: AuthService = Api.retrofit.create(AuthService::class.java)

    suspend fun login(admin: Admin): Result<TokenHolder> {
        try {
            return Result.success(authService.login(admin))
        } catch (e: Exception) {
            println(e)
            return Result.failure(e)
        }
    }
}

