package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

/**
 * Authenticationscreen: The entry point landing page for unauthenticated users.
 * Provides a welcome message and options to either Start Fresh (Sign Up) 
 * or continue with an existing account (Login).
 */
class Authenticationscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable Edge-to-Edge for a modern immersive look
        enableEdgeToEdge()
        setContentView(R.layout.activity_authenticationscreen)

        setupEdgeToEdge()
        setupClickListeners()
    }

    private fun setupEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun setupClickListeners() {
        // Navigate to Sign Up screen
        findViewById<MaterialButton>(R.id.get_started_button).setOnClickListener {
            val intent = Intent(this, Signupscreen::class.java)
            startActivity(intent)
        }

        // Navigate to Login screen
        findViewById<TextView>(R.id.login_link).setOnClickListener {
            val intent = Intent(this, Loginscreen::class.java)
            startActivity(intent)
        }
    }
}