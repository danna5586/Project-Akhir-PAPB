package com.tifd.marketplacekampus

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Favorites : BottomNavItem("favorites", "Favorites", Icons.Filled.Star)
    object Riwayat : BottomNavItem("riwayat", "Riwayat", Icons.Filled.History)
    object Cart : BottomNavItem("cart", "Cart", Icons.Filled.ShoppingCart)
}

@Composable
fun AppNavigation(auth: FirebaseAuth) {
    val navController = rememberNavController()
    val favorites = remember { mutableStateOf<List<Item>>(emptyList()) }
    val cartItems = remember { mutableStateOf<List<Item>>(emptyList()) } // State untuk keranjang
    val purchasedItems = remember { mutableStateOf<List<Item>>(emptyList()) } // State untuk riwayat pembelian
    val currentRoute = remember { mutableStateOf("login") }

    // Monitor perubahan currentRoute
    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentRoute.value = destination.route ?: "login"
    }

    Scaffold(
        bottomBar = {
            // Bottom bar hanya muncul jika tidak di layar login/register
            if (currentRoute.value != "login" && currentRoute.value != "register") {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") {
                MyScreen(auth, navController)
            }
            composable("register") {
                RegisterScreen(auth, navController)
            }
            composable(BottomNavItem.Home.route) {
                HomeScreen(auth, navController, favorites, cartItems) // Kirim cartItems
            }
            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen(favorites.value, navController, onAddToCart = { item ->
                    // Fungsi untuk menambahkan item ke cart
                    cartItems.value = cartItems.value + item
                })
            }
            composable(BottomNavItem.Riwayat.route) {
                RiwayatScreen(purchasedItems.value) // Menampilkan item yang telah dibeli
            }
            composable(BottomNavItem.Cart.route) {
                CartScreen(
                    cartItems = cartItems,
                    navController = navController,
                    onCheckout = { items ->
                        purchasedItems.value = items // Pindahkan item ke riwayat
                        cartItems.value = emptyList() // Kosongkan keranjang setelah checkout
                        navController.navigate(BottomNavItem.Riwayat.route) // Navigasi ke RiwayatScreen
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavHostController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Riwayat,
        BottomNavItem.Cart
    )
    NavigationBar {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
