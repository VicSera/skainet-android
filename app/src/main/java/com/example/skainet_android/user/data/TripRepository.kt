package com.example.skainet_android.trip.data

import android.util.Log
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.Trip
import com.example.skainet_android.user.data.remote.TripApi

object TripRepository {
    private var cachedTrips: MutableList<Trip>? = null;

    suspend fun loadAll(): Result<List<Trip>> {
        if (cachedTrips != null) {
            Log.v(TAG, "loadAll - return cached trips")
            return Result.success(cachedTrips as List<Trip>);
        }
        try {
            Log.v(TAG, "loadAll - started")
            val trips = TripApi.service.find()
            Log.v(TAG, "loadAll - succeeded")
            cachedTrips = mutableListOf()
            cachedTrips?.addAll(trips)
            return Result.success(cachedTrips as List<Trip>)
        } catch (e: Exception) {
            Log.w(TAG, "loadAll - failed", e)
            return Result.failure(e)
        }
    }

    suspend fun load(tripId: String): Result<Trip> {
        val trip = cachedTrips?.find { it.id == tripId }
        if (trip != null) {
            Log.v(TAG, "load - return cached trip")
            return Result.success(trip)
        }
        try {
            Log.v(TAG, "load - started")
            val tripRead = TripApi.service.read(tripId)
            Log.v(TAG, "load - succeeded")
            return Result.success(tripRead)
        } catch (e: Exception) {
            Log.w(TAG, "load - failed", e)
            return Result.failure(e)
        }
    }

    suspend fun save(trip: Trip): Result<Trip> {
        try {
            Log.v(TAG, "save - started")
            val createdTrip = TripApi.service.create(trip)
            Log.v(TAG, "save - succeeded")
            cachedTrips?.add(createdTrip)
            return Result.success(createdTrip)
        } catch (e: Exception) {
            Log.w(TAG, "save - failed", e)
            return Result.failure(e)
        }
    }

    suspend fun update(trip: Trip): Result<Trip> {
        try {
            Log.v(TAG, "update - started")
            val updatedTrip = TripApi.service.update(trip)
            val index = cachedTrips?.indexOfFirst { it.id == trip.id }
            if (index != null) {
                cachedTrips?.set(index, updatedTrip)
            }
            Log.v(TAG, "update - succeeded")
            return Result.success(updatedTrip)
        } catch (e: Exception) {
            Log.v(TAG, "update - failed")
            return Result.failure(e)
        }
    }
}