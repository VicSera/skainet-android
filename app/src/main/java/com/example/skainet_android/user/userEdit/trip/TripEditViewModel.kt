package com.example.skainet_android.user.userEdit.trip

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.skainet_android.core.TAG
import com.example.skainet_android.user.data.User
import kotlinx.coroutines.launch
import com.example.skainet_android.core.Result
import com.example.skainet_android.user.data.Trip
import com.example.skainet_android.user.data.remote.TripApi
import com.example.skainet_android.user.data.remote.UserApi
import kotlinx.coroutines.runBlocking

class TripEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    fun getItemById(tripId: String): LiveData<Trip> {
        Log.v(TAG, "getTripById...")
        val data = MutableLiveData<Trip>(null)

        runBlocking {
            viewModelScope.launch {
                data.value = TripApi.service.read(tripId)
            }
        }
        return data
    }

    fun saveOrUpdateTrip(trip: Trip) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateTrip...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Trip> = if (trip.id.isNotEmpty()) {
                Result.Success(TripApi.service.update(trip))
            } else {
                Result.Success(TripApi.service.create(trip))
            }
            when(result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateTrip succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateTrip failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}