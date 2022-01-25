package com.example.skainet_android.user.data

import android.util.Log
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.remote.TripApi
import com.example.skainet_android.user.data.remote.UserApi

object UserRepository {
    private var cachedUsers: MutableList<User>? = null;

    suspend fun loadAll(): Result<List<User>> {
        if (cachedUsers != null) {
            Log.v(TAG, "loadAll - return cached users")
            return Result.success(cachedUsers as List<User>);
        }
        return try {
            Log.v(TAG, "loadAll - started")
            val users = UserApi.service.find()
            Log.v(TAG, "loadAll - succeeded")
            cachedUsers = users.toMutableList()

            Result.success(cachedUsers as List<User>)
        } catch (e: Exception) {
            Log.w(TAG, "loadAll - failed", e)

            Result.failure(e)
        }
    }

    suspend fun load(userId: String): Result<User> {
        val user = cachedUsers?.find { it.id == userId }
        if (user?.trips != null) {
            Log.v(TAG, "load - return cached user")
            return Result.success(user)
        }
        return try {
            Log.v(TAG, "load - started")
            val userRead = UserApi.service.read(userId)
            val trips = TripApi.service.findForUser(userId)
            userRead.trips = trips
            Log.v(TAG, "load - succeeded")

            Result.success(userRead)
        } catch (e: Exception) {
            Log.w(TAG, "load - failed", e)

            Result.failure(e)
        }
    }

    suspend fun save(user: User): Result<User> {
        return try {
            Log.v(TAG, "save - started")
            val createdUser = UserApi.service.create(user)
            Log.v(TAG, "save - succeeded")
            cachedUsers?.add(createdUser)

            Result.success(createdUser)
        } catch (e: Exception) {
            Log.w(TAG, "save - failed", e)

            Result.failure(e)
        }
    }

    suspend fun update(user: User): Result<User> {
        try {
            Log.v(TAG, "update - started")
            val updatedUser = UserApi.service.update(user)
            val index = cachedUsers?.indexOfFirst { it.id == user.id }
            if (index != null) {
                cachedUsers?.set(index, updatedUser)
            }
            Log.v(TAG, "update - succeeded")
            return Result.success(updatedUser)
        } catch (e: Exception) {
            Log.v(TAG, "update - failed")
            return Result.failure(e)
        }
    }
}