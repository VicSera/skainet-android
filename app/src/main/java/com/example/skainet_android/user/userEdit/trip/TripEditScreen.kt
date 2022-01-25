package com.example.skainet_android.trip.tripEdit.trip

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.compose.rememberNavController
import com.example.skainet_android.R
import com.example.skainet_android.user.data.Trip
import com.example.skainet_android.user.userEdit.EditViewModelFactory

val TAG = "TripScreen"

@Composable
fun TripEditScreen(navController: NavController, tripId: String? = null) {
    val tripEditViewModel = viewModel<TripEditViewModel>(factory = EditViewModelFactory(tripId))
    val fetching = tripEditViewModel.fetching.observeAsState().value
    val fetchingError = tripEditViewModel.fetchingError.observeAsState().value
    val completed = tripEditViewModel.completed.observeAsState().value
    var submitStarted by remember { mutableStateOf(false) }
    val loadedTrip = tripEditViewModel.trip

    var trip by remember { mutableStateOf(Trip()) }

    loadedTrip.value?.let {
        if (tripId != "new" && trip.id.isEmpty())
            trip = it
    }

    Log.d(TAG, "recompose $completed $submitStarted");
    if (completed != null) {
        if (submitStarted && completed) {
            Log.d(TAG, "navigate")
            submitStarted = false;
            navController.navigate("userEdit/${trip.userId}")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.trip_edit)) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d(TAG, "save $trip");
                    submitStarted = true
                    tripEditViewModel.saveOrUpdateTrip(trip)
                },
            ) { Icon(imageVector = Icons.Rounded.Save, contentDescription = "Save") }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            TextField(
                label = { Text(text = "Name") },
                value = trip.name,
                onValueChange = { trip = trip.copy(name = it) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                label = { Text(text = "User ID") },
                value = trip.userId,
                onValueChange = { trip = trip.copy(userId = it) },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                label = { Text(text = "Datetime") },
                value = trip.datetime,
                onValueChange = { trip = trip.copy(datetime = it) },
                modifier = Modifier.fillMaxWidth()
            )
            if (fetching != null && fetching) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                );
            }
            if (fetchingError != null) {
                Text(text = "Fetching failed")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripScreenPreview() {
    TripEditScreen(rememberNavController())
}
