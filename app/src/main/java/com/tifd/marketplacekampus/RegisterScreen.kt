package com.tifd.marketplacekampus

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun RegisterScreen(auth: FirebaseAuth, navController: NavHostController) {
    var inputText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var isFormFilled = inputText.isNotBlank() && passwordText.isNotBlank()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Card untuk teks Register yang berada di tengah
        Card(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Text(
                text = "Daftar Akun Baru",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold, color = Color.White),
                modifier = Modifier
                    .padding(16.dp) // Memberi padding di dalam Card
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Form input untuk email
        InputForm(
            icon = Icons.Filled.AccountBox,
            label = "Masukkan email",
            value = inputText,
            onValueChange = { inputText = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Form input untuk password
        InputForm(
            icon = Icons.Filled.Lock,
            label = "Masukkan password",
            value = passwordText,
            onValueChange = { passwordText = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button untuk Register
        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(inputText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Pendaftaran Berhasil", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") // Navigasi kembali ke login setelah pendaftaran berhasil
                        } else {
                            Toast.makeText(
                                context,
                                "Pendaftaran Gagal: ${task.exception?.localizedMessage}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = isFormFilled,
            colors = ButtonDefaults.buttonColors(containerColor = if (isFormFilled) Color.Black else Color.Gray)
        ) {
            Text("Daftar", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }
    }
}
