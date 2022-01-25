package com.example.skainet_android.user.userEdit

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.compose.rememberNavController
import com.example.skainet_android.R
import com.example.skainet_android.user.data.Trip
import com.example.skainet_android.user.data.User

val TAG = "UserScreen"

@Composable
fun UserEditScreen(navController: NavController, userId: String? = null) {
    val userEditViewModel = viewModel<UserEditViewModel>(factory = EditViewModelFactory(userId))
    val fetching = userEditViewModel.fetching.observeAsState().value
    val fetchingError = userEditViewModel.fetchingError.observeAsState().value
    val completed = userEditViewModel.completed.observeAsState().value
    val loadedUser = userEditViewModel.user
    var submitStarted by remember { mutableStateOf(false) }

    var user by remember { mutableStateOf(User()) }

    loadedUser.value?.let {
        if (userId != "new" && user.id.isEmpty())
            user = it.copy()
    }
    Log.d(TAG, user.toString())

    Log.d(TAG, "recompose $completed $submitStarted")
    if (completed != null) {
        if (submitStarted && completed) {
            Log.d(TAG, "navigate")
            submitStarted = false
            navController.navigate("userList")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(id = R.string.user_edit)) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d(TAG, "save $user");
                    submitStarted = true
                    userEditViewModel.saveOrUpdateUser(user)
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
                value = user.name,
                onValueChange = { user = user.copy(name = it) },
                modifier = Modifier.fillMaxWidth()
            )
            
            Text(text = "Trips")
            Button(onClick = { navController.navigate("tripEdit/new") }) {
                Text(text = "Add trip")
            }
            if (user.trips != null)
                TripList(user.trips!!, navController)

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

@Composable
fun TripList(tripList: List<Trip>, navController: NavController) {
    LazyColumn {
        items(tripList) { trip ->
            TripDetail(trip, navController)
        }
    }
}

@Composable
fun TripDetail(trip: Trip, navController: NavController) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Button(onClick = { navController.navigate("tripEdit/${trip.id}") }) {
            Column {
                Text(
                    text = trip.toString()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserScreenPreview() {
    UserEditScreen(rememberNavController())
}