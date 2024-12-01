package com.tifd.marketplacekampus

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(auth: FirebaseAuth, navController: NavHostController, favorites: MutableState<List<Item>>) {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                            // Menambah atau menghapus item dari favorit
                            if (favorites.value.contains(item)) {
                                favorites.value = favorites.value.filterNot { it == item }
                            } else {
                                favorites.value = favorites.value + item
                            }
                        }
                    )
                }
            }
        }

        // IconButton untuk Search di pojok kanan atas
        IconButton(
            onClick = { isSearchVisible = !isSearchVisible },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(Color.Black, shape = RoundedCornerShape(50))
                .padding(0.dp)
        ) {
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        // Tombol untuk melihat barang favorit
        IconButton(
            onClick = { navController.navigate("favorites") },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {
            Icon(Icons.Filled.Star, contentDescription = "Favorites", tint = Color.Yellow)
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Button(
                onClick = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Logout", color = Color.White)
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
    onFavoriteClick: (Item) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Memberikan padding di sekitar card
        shape = RoundedCornerShape(12.dp), // Membuat sudut card menjadi bulat
        colors = CardDefaults.cardColors(containerColor = Color.LightGray) // Warna latar belakang abu terang
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // Padding di dalam card
        ) {
            // Menampilkan informasi barang di dalam card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Nama: ${item.name}", style = MaterialTheme.typography.bodyLarge)
                Text("Harga: ${item.price}", style = MaterialTheme.typography.bodyMedium)
                Text("Deskripsi: ${item.description}", style = MaterialTheme.typography.bodySmall)

                // Status Barang di posisi kanan tengah dan dengan ukuran font yang lebih besar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, // Mengatur status agar berada di kanan
                    verticalAlignment = Alignment.CenterVertically // Memastikan status berada di tengah vertikal
                ) {
                    Text(
                        text = item.status,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 18.sp, // Menyesuaikan ukuran font
                            color = if (item.status == "Tersedia") Color.Green else Color.Red
                        )
                    )
                }
            }

            // Ikon favorit di pojok kanan atas
            IconButton(
                onClick = { onFavoriteClick(item) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp) // Memberikan padding di sekitar ikon
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Yellow else Color.Gray
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
