package com.example.skainet_android.user.userEdit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.User
import com.example.skainet_android.user.data.UserRepository
import com.example.skainet_android.user.data.local.UserDatabase
import kotlinx.coroutines.launch
import com.example.skainet_android.core.Result
import com.example.skainet_android.user.data.remote.UserApi
import kotlinx.coroutines.runBlocking

class UserEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val userRepository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application, viewModelScope).userDao()
        userRepository = UserRepository(userDao)
    }

    fun getItemById(userId: String): LiveData<User> {
        Log.v(TAG, "getItemById...")
        val data = MutableLiveData<User>(null)

        runBlocking {
            viewModelScope.launch {
                data.value = UserApi.service.read(userId)
            }
        }
        return data
//        return userRepository.getById(itemId)
    }

    fun saveOrUpdateUser(user: User) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateUser...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<User> = if (user.id.isNotEmpty()) {
                userRepository.update(user)
            } else {
                userRepository.save(user)
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateUser succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateUser failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}