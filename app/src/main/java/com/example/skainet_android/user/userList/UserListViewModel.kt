package com.example.skainet_android.user.userList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.User
import com.example.skainet_android.user.data.UserRepository
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {
    private val mutableUsers = MutableLiveData<List<User>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Throwable>().apply { value = null }

    val users: LiveData<List<User>> = mutableUsers
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Throwable> = mutableException

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            Log.v(TAG, "loadUsers...");
            mutableLoading.value = true
            mutableException.value = null
            val result = UserRepository.loadAll()
            if (result.isSuccess) {
                Log.d(TAG, "loadUsers succeeded");
                mutableUsers.value = result.getOrNull()
            }
            if (result.isFailure) {
                Log.w(TAG, "loadUsers failed", result.exceptionOrNull());
                mutableException.value = result.exceptionOrNull()
            }
            mutableLoading.value = false
        }
    }
}