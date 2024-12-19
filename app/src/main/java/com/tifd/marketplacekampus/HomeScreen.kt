package com.tifd.marketplacekampus

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(
    auth: FirebaseAuth,
    navController: NavHostController,
    favorites: MutableState<List<Item>>,
    cartItems: MutableState<List<Item>>
) {
    var items by remember { mutableStateOf<List<Item>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(true) {
        db.collection("items").get()
            .addOnSuccessListener { result ->
                val fetchedItems = mutableListOf<Item>()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val price = document.getString("price") ?: ""
                    val description = document.getString("description") ?: ""
                    val status = document.getString("status") ?: "Tersedia"
                    fetchedItems.add(Item(name, price, description, status))
                }
                items = fetchedItems
            }
            .addOnFailureListener { exception ->
                Log.e("HomeScreen", "Error getting documents: ", exception)
            }
    }

    val filteredItems = items.filter { item ->
        item.name.contains(searchQuery, ignoreCase = true) ||
                item.description.contains(searchQuery, ignoreCase = true)
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (!isSearchVisible) {
                Text("Selamat Datang di Marketplace Kampus!", style = MaterialTheme.typography.headlineMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isSearchVisible) {
                SearchBar(searchQuery = searchQuery, onSearchQueryChanged = { searchQuery = it })
            }

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredItems) { item ->
                    ItemCard(
                        item = item,
                        isFavorite = favorites.value.contains(item),
                        onFavoriteClick = {
                            if (favorites.value.contains(item)) {
                                favorites.value = favorites.value.filterNot { it == item }
                            } else {
                                favorites.value = favorites.value + item
                            }
                        },
                        onAddToCart = {
                            cartItems.value = cartItems.value + item
                        }
                    )
                }
            }
        }

        IconButton(
            onClick = { isSearchVisible = !isSearchVisible },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        }

        Box(modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp)) {
            Button(onClick = {
                auth.signOut()
                navController.navigate("login") { popUpTo("home") { inclusive = true } }
            },colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        label = { Text("Cari barang...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp)),
        singleLine = true
    )
}

@Composable
fun ItemCard(
    item: Item,
    isFavorite: Boolean = false,
    onFavoriteClick: (Item) -> Unit,
    onAddToCart: (Item) -> Unit
) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Nama: ${item.name}")
                Text("Harga: ${item.price}")
                Text("Deskripsi: ${item.description}")

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = item.status,
                        color = if (item.status == "Tersedia") Color.Green else Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Ikon keranjang di pojok kanan atas
            IconButton(
                onClick = {
                    if (item.status == "Terjual") {
                        // Menampilkan notifikasi jika item sudah terjual
                        Toast.makeText(context, "Item ini sudah terjual", Toast.LENGTH_SHORT).show()
                    } else {
                        // Menambahkan item ke keranjang jika status Tersedia
                        onAddToCart(item)
                    }
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Add to Cart",
                    tint = if (item.status == "Terjual") Color.Gray else Color.Black // Menonaktifkan ikon jika sudah terjual
                )
            }

            // Ikon favorit di pojok kiri bawah
            IconButton(
                onClick = { onFavoriteClick(item) },
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}

data class Item(
    val name: String = "",
    val price: String = "",
    val description: String = "",
    val status: String = ""
)
