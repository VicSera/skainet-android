package com.example.skainet_android.user.data.remote

import com.example.skainet_android.core.Api
import com.example.skainet_android.user.data.User
import retrofit2.http.*

object UserApi {
    interface Service {
        @GET("/api/users")
        suspend fun find(): List<User>

        @GET("/api/users/{id}")
        suspend fun read(@Path("id") userId: String): User;

        @Headers("Content-Type: application/json")
        @POST("/api/users")
        suspend fun create(@Body user: User): User

        @Headers("Content-Type: application/json")
        @PUT("/api/users")
        suspend fun update(@Body user: User): User
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}