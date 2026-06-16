package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    // This creates a secure variable slot to hold our Firebase Auth connection
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This hooks up your app to the Firebase cloud server using your json file
        auth = Firebase.auth

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // This launches the custom Login Screen interface we are about to build below
                    LoginComponent(auth = auth, onLoginSuccess = {
                        Toast.makeText(this, "Welcome to Taskmaster!", Toast.LENGTH_SHORT).show()

                        // Navigate to ProjectBoardScreen Activity
                        val intent = Intent(this, ProjectBoardScreen::class.java)
                        startActivity(intent)
                        finish()
                    })
                }
            }
        }
    }
}
@Composable
fun LoginComponent(auth: FirebaseAuth, onLoginSuccess: () -> Unit) {
    // These hold what the user types into the boxes
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Taskmaster", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Sign in to manage your projects", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Email Text Box
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Text Box
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator() // Shows a loading wheel while checking with Firebase
        } else {
            // Sign In Button
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        errorMessage = ""

                        // THIS LINE: Sends the email and password over the internet to Firebase
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {android.util.Log.d("TASKMASTER_AUTH", "User email is: ${auth.currentUser?.email}")
                                    onLoginSuccess() // Success! Runs the code in step 3
                                } else {
                                    // If something goes wrong (wrong password, no internet), show the error
                                    errorMessage = task.exception?.localizedMessage ?: "Authentication failed."
                                }
                            }
                    } else {
                        errorMessage = "Please enter both an email and password."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign In")
            }
        }

        // Show error text if there is an issue
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}