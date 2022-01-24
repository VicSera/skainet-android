package com.example.skainet_android.user.data

import androidx.lifecycle.LiveData
import com.example.skainet_android.user.data.remote.UserApi
import com.example.skainet_android.user.data.local.UserDao
import com.example.skainet_android.core.Result

class UserRepository(private val userDao: UserDao) {

    val users = userDao.getAll()

    suspend fun refresh(): Result<Boolean> {
        try {
            val users = UserApi.service.find()
            for (user in users) {
                userDao.insert(user)
            }
            return Result.Success(true)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<User> {
        return userDao.getById(itemId)
    }

    suspend fun save(user: User): Result<User> {
        try {
            val createdUser = UserApi.service.create(user)
            userDao.insert(createdUser)
            return Result.Success(createdUser)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }

    suspend fun update(user: User): Result<User> {
        try {
            val updatedUser = UserApi.service.update(user)
            userDao.update(updatedUser)
            return Result.Success(updatedUser)
        } catch(e: Exception) {
            return Result.Error(e)
        }
    }
}