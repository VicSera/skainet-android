package com.example.skainet_android.user.userEdit

import android.util.Log
import androidx.lifecycle.*
import com.example.skainet_android.user.data.User
import com.example.skainet_android.user.data.UserRepository
import kotlinx.coroutines.launch

class UserEditViewModel(userId: String? = null) : ViewModel() {
    private val mutableUser = MutableLiveData<User>().apply { value = null }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Throwable>().apply { value = null }

    val user: LiveData<User> = mutableUser
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Throwable> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    init {
        userId?.let { if (userId != "new") loadUser(it) }
    }

    private fun loadUser(userId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadUser $userId...")
            mutableFetching.value = true
            mutableException.value = null
            val result = UserRepository.load(userId)
            if (result.isSuccess) {
                Log.d(TAG, "loadUser succeeded");
                mutableUser.value = result.getOrNull()
                Log.d(TAG, "Loaded user: ${result.getOrNull()?.toString() ?: ""}")
            }
            if (result.isFailure) {
                Log.w(TAG, "loadUser failed", result.exceptionOrNull());
                mutableException.value = result.exceptionOrNull()
            }
        }
        mutableFetching.value = false

    }

    fun saveOrUpdateUser(user: User) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateUser...")
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<User> = if (user.id.isNotEmpty()) {
                UserRepository.update(user)
            } else {
                UserRepository.save(user)
            }
            if (result.isSuccess) {
                Log.d(TAG, "saveOrUpdateUser succeeded");
                mutableUser.value = result.getOrNull()
            }
            if (result.isFailure) {
                Log.w(TAG, "saveOrUpdateUser failed", result.exceptionOrNull());
                mutableException.value = result.exceptionOrNull()
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}

