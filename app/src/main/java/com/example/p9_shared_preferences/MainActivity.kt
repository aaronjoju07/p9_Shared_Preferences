package com.example.p9_shared_preferences

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.p9_shared_preferences.ui.theme.P9_Shared_PreferencesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            P9_Shared_PreferencesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoggedOut by remember { mutableStateOf(false) }
    val storedEmail by EmailDataStore.getEmail(context).collectAsState(initial = null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoggedOut || storedEmail == null) {
            // User is not logged in, show login fields
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Handle login button click
                    // For now, just print the email and password
                    println("Email: $email, Password: $password")

                    // Store the email using CoroutineScope
                    CoroutineScope(Dispatchers.IO).launch {
                        EmailDataStore.setEmail(context, email)
                    }
                    isLoggedOut = false
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Login")
            }
        } else {
            // User is logged in, display logged in message and logout button
            Text("Logged in as: $storedEmail", modifier = Modifier.padding(vertical = 8.dp))

            Button(
                onClick = {
                    // Handle logout button click
                    // Delete stored email
                    CoroutineScope(Dispatchers.IO).launch {
                        EmailDataStore.setEmail(context, "")
                    }
                    // Reset the state to indicate logged out
                    isLoggedOut = true
                },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Logout")
            }
        }
    }
}
