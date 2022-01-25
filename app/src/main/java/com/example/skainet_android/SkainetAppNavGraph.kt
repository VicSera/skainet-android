package com.example.skainet_android

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skainet_android.auth.login.LoginScreen
import com.example.skainet_android.trip.tripEdit.trip.TripEditScreen
import com.example.skainet_android.user.userEdit.UserEditScreen
import com.example.skainet_android.user.userList.UserListScreen

@Composable
fun SkainetAppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = "login"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable("userList") {
            UserListScreen(navController = navController)
        }
        composable("userEdit/{userId}", arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
            UserEditScreen(navController = navController, backStackEntry.arguments?.getString("userId"))
        }
        composable("tripEdit/{tripId}", arguments = listOf(navArgument("tripId") { type = NavType.StringType })) { backStackEntry ->
            TripEditScreen(navController = navController, backStackEntry.arguments?.getString("tripId"))
        }
        composable("login") {
            LoginScreen(navController = navController)
        }
    }
}