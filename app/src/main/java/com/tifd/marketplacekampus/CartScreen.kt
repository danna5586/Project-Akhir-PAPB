package com.tifd.marketplacekampus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun CartScreen(
    cartItems: MutableState<List<Item>>,
    navController: NavHostController,
    onCheckout: (List<Item>) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Keranjang Belanja",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp) // Memberikan padding bawah
        )
        if (cartItems.value.isEmpty()) {
            Text("Keranjang Anda kosong.", style = MaterialTheme.typography.bodyMedium)
        } else {
            // Menggunakan LazyColumn untuk menggulirkan item
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems.value) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nama: ${item.name}")
                            Text("Harga: ${item.price}")
                            Text("Deskripsi: ${item.description}")

                            // Menambahkan ikon hapus
                            Spacer(modifier = Modifier.height(8.dp))
                            IconButton(
                                onClick = {
                                    // Hapus item dari keranjang
                                    cartItems.value = cartItems.value.filter { it != item }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Hapus Item",
                                    tint = Color.Black // Mengatur warna ikon menjadi hitam
                                )
                            }
                        }
                    }
                }
            }

            // Tombol Checkout berada di bawah LazyColumn
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    onCheckout(cartItems.value)  // Pindahkan item ke riwayat dan kosongkan keranjang
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black) // Mengatur warna tombol menjadi hitam
            ) {
                Text("Checkout", color = Color.White) // Mengatur warna teks tombol menjadi putih
            }
        }
    }
}
