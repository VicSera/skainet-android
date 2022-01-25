package com.example.skainet_android.trip.tripEdit.trip

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skainet_android.core.TAG
import com.example.skainet_android.trip.data.TripRepository
import com.example.skainet_android.user.data.Trip
import kotlinx.coroutines.launch

class TripEditViewModel(tripId: String?) : ViewModel() {
    private val mutableTrip = MutableLiveData<Trip>().apply { value = null }
    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Throwable>().apply { value = null }

    val trip: LiveData<Trip> = mutableTrip
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Throwable> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    init {
        tripId?.let { if (tripId != "new") loadTrip(it) }
    }

    private fun loadTrip(tripId: String) {
        viewModelScope.launch {
            Log.i(TAG, "loadTrip...")
            mutableFetching.value = true
            mutableException.value = null
            val result = TripRepository.load(tripId)
            if (result.isSuccess) {
                Log.d(TAG, "loadTrip succeeded");
                mutableTrip.value = result.getOrNull()
            }
            if (result.isFailure) {
                Log.w(TAG, "loadTrip failed", result.exceptionOrNull());
                mutableException.value = result.exceptionOrNull()
            }
        }
        mutableFetching.value = false
    }

    fun saveOrUpdateTrip(trip: Trip) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateTrip...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Trip> = if (trip.id.isNotEmpty()) {
                TripRepository.update(trip)
            } else {
                TripRepository.save(trip)
            }
            if (result.isSuccess) {
                Log.d(TAG, "saveOrUpdateTrip succeeded");
                mutableTrip.value = result.getOrNull()
            }
            if (result.isFailure) {
                Log.w(TAG, "saveOrUpdateTrip failed", result.exceptionOrNull());
                mutableException.value = result.exceptionOrNull()
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }
}