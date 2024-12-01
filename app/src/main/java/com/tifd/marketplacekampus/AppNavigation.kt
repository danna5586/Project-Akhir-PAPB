package com.tifd.marketplacekampus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavigation(auth: FirebaseAuth) {
    val navController = rememberNavController()
    val favorites = remember { mutableStateOf<List<Item>>(emptyList()) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            MyScreen(auth, navController)
        }
        composable("home") {
            HomeScreen(auth, navController, favorites)
        }
        composable("favorites") {
            FavoritesScreen(favorites.value, navController)
        }
        composable("register") {
            RegisterScreen(auth, navController)
        }
    }
}