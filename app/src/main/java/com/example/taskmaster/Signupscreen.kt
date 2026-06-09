package com.example.taskmaster

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Signupscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signupscreen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameLayout = findViewById<TextInputLayout>(R.id.name_layout)
        val emailLayout = findViewById<TextInputLayout>(R.id.email_layout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.password_layout)
        
        val nameEditText = findViewById<TextInputEditText>(R.id.name_edit_text)
        val emailEditText = findViewById<TextInputEditText>(R.id.email_edit_text)
        val passwordEditText = findViewById<TextInputEditText>(R.id.password_edit_text)
        
        val signupButton = findViewById<MaterialButton>(R.id.signup_button)
        val loginText = findViewById<TextView>(R.id.login_text)

        signupButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInputs(name, email, password, nameLayout, emailLayout, passwordLayout)) {
                signupButton.isEnabled = false
                signupButton.text = getString(R.string.creating_account)

                Handler(Looper.getMainLooper()).postDelayed({
                    Toast.makeText(this, "Account Created! Please Login.", Toast.LENGTH_SHORT).show()
                    finish() // Go back to login
                }, 1500)
            }
        }

        loginText.setOnClickListener {
            finish()
        }
    }

    private fun validateInputs(
        name: String,
        email: String,
        password: String,
        nameLayout: TextInputLayout,
        emailLayout: TextInputLayout,
        passwordLayout: TextInputLayout
    ): Boolean {
        var isValid = true
        
        nameLayout.error = null
        emailLayout.error = null
        passwordLayout.error = null

        if (name.isEmpty()) {
            nameLayout.error = getString(R.string.name_required)
            isValid = false
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = getString(R.string.invalid_email)
            isValid = false
        }

        if (password.length < 6) {
            passwordLayout.error = getString(R.string.invalid_password)
            isValid = false
        }

        return isValid
    }
}