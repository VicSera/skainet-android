package com.example.skainet_android.user.userList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.skainet_android.user.data.User
import com.example.skainet_android.core.Result
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.UserRepository
import com.example.skainet_android.user.data.local.UserDatabase
import kotlinx.coroutines.launch

class UserListViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val users: LiveData<List<User>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    val userRepository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application, viewModelScope).userDao()
        userRepository = UserRepository(userDao)
        users = userRepository.users
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = userRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}