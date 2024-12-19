package com.tifd.marketplacekampus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun FavoritesScreen(favorites: List<Item>, navController: NavHostController, onAddToCart: (Item) -> Unit) {
    // Tampilkan daftar favorit
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Menambahkan judul "Favorite" di atas
        Text(
            text = "Favorite",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp) // Memberikan padding bawah
        )

        // Menampilkan pesan jika daftar favorit kosong
        if (favorites.isEmpty()) {
            Text(
                text = "Favorite Anda masih kosong",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black // Memberikan warna abu-abu untuk teks
            )
        } else {
            // LazyColumn memungkinkan daftar digulir
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(favorites) { item ->
                    // Tampilan item favorit
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Nama item
                                Text("Nama: ${item.name}", style = MaterialTheme.typography.bodyMedium)

                                // Harga item
                                Text("Harga: ${item.price}", style = MaterialTheme.typography.bodyMedium)

                                // Deskripsi item
                                Text("Deskripsi: ${item.description}", style = MaterialTheme.typography.bodyMedium)

                                // Status item
                                Text(
                                    "Status: ${item.status}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (item.status == "Tersedia") Color.Green else Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
