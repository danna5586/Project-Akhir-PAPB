package com.tifd.marketplacekampus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun FavoritesScreen(favorites: List<Item>, navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Tombol kembali menggunakan ikon panah
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(favorites) { item ->
                ItemCard(
                    item = item,
                    isFavorite = true, // Semua item di favoritkan, jadi ini true
                    onFavoriteClick = {} // Tidak perlu aksi klik untuk layar favorit
                )
            }
        }
    }
}

