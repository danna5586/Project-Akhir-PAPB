package com.tifd.marketplacekampus

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.tifd.marketplacekampus.ui.theme.MarketplaceKampusTheme
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Inisialisasi Firebase Auth

        setContent {
            MarketplaceKampusTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(auth) // Memanggil fungsi AppNavigation
                }
            }
        }
    }
}

@Composable
fun MyScreen(auth: FirebaseAuth, navController: NavHostController) {
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
        // Card untuk teks Login yang berada di tengah
        Card(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            // Teks Login di dalam Card
            Text(
                text = "Marketplace Kampus",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold,color = Color.White),
                modifier = Modifier
                    .padding(16.dp) // Memberi padding di dalam Card
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Card for user name input
        InputForm(
            icon = Icons.Filled.AccountBox,
            label = "Masukkan gmail",
            value = inputText,
            onValueChange = { inputText = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password input field (NIM changed to password)
        InputForm(
            icon = Icons.Filled.Lock,
            label = "Masukkan password",
            value = passwordText,
            onValueChange = { passwordText = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(
            onClick = {
                auth.signInWithEmailAndPassword(inputText, passwordText)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            navController.navigate("home") // Navigasi ke halaman Home
                        } else {
                            Toast.makeText(
                                context,
                                "Login Gagal: ${task.exception?.localizedMessage}",
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
            Text("Login", style = MaterialTheme.typography.bodyMedium, color = Color.White)
        }
    }
}

@Composable
fun InputForm(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            tint = Color.Black, // Set icon color to black
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            singleLine = true
        )
    }
}

@Composable
fun AppNavigation(auth: FirebaseAuth) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            MyScreen(auth, navController)
        }
        composable("home") {
            HomeScreen(auth, navController)
        }
    }
}


