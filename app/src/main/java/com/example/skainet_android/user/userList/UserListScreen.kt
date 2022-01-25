package com.example.skainet_android.user.userList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import com.example.skainet_android.R
import com.example.skainet_android.user.data.User
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.delay

@ExperimentalAnimationApi
@Composable
fun UserListScreen(navController: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.user_list)) }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("userEdit/new")
                },
            ) { Icon(imageVector = Icons.Rounded.Add, contentDescription = "Save") }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            val loginViewModel = viewModel<UserListViewModel>()
            val loading = loginViewModel.loading.observeAsState().value
            val userList = loginViewModel.users.observeAsState().value
            val loadingError = loginViewModel.loadingError.observeAsState().value
            if (loading != null && loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(15.dp)
                )
            } else if (loadingError != null) {
                Text(text = "Loading failed")
            } else {
                UserList(userList!!, navController)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun UserList(userList: List<User>, navController: NavController) {
    LazyColumn {
        items(userList) {user ->
            UserDetail(user, navController)
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun UserDetail(user: User, navController: NavController) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        AnimatedVisibility(
            visible = true,
            enter = slideInHorizontally({ -it }),
            exit = slideOutHorizontally({ -it }),

            ) {
            Button(onClick = { navController.navigate("userEdit/${user.id}") }) {
                Column {
                    Text(
                        text = user.toString()
                    )
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun UsersScreenPreview() {
    UserListScreen(rememberNavController())
}