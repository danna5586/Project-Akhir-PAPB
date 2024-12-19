package com.tifd.marketplacekampus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

@Composable
fun RiwayatScreen(purchasedItems: List<Item>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Riwayat Pembelian",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp) // Memberikan padding bawah
        )

        if (purchasedItems.isEmpty()) {
            Text("Tidak ada pembelian", style = MaterialTheme.typography.bodyMedium)
        } else {
            // Menampilkan daftar item yang dibeli menggunakan LazyColumn
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(purchasedItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Text("Nama: ${item.name}")
                            Text("Harga: ${item.price}")
                            Text("Deskripsi: ${item.description}")
                        }
                    }
                }
            }
        }
    }
}
