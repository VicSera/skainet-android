package com.example.skainet_android.user.data.remote

import com.example.skainet_android.core.Api
import com.example.skainet_android.user.data.Trip
import com.example.skainet_android.user.data.User
import retrofit2.http.*

object TripApi {
    interface Service {
        @GET("/api/trips/{id}")
        suspend fun read(@Path("id") tripId: String): Trip;

        @Headers("Content-Type: application/json")
        @POST("/api/trips")
        suspend fun create(@Body trip: Trip): Trip

        @Headers("Content-Type: application/json")
        @PUT("/api/trips")
        suspend fun update(@Body trip: Trip): Trip
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}