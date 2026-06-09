package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Loginscreen : AppCompatActivity() {

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginscreen)

        initViews()
        setupListeners()
    }

    private fun initViews() {
        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        emailLayout = findViewById(R.id.email_layout)
        passwordLayout = findViewById(R.id.password_layout)
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
    }

    private fun setupListeners() {
        // Clear errors as user types (Authentic UX)
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailLayout.error = null
                passwordLayout.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        }
        
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        loginButton.setOnClickListener {
            handleLogin()
        }

        findViewById<TextView>(R.id.sign_up_text).setOnClickListener {
            val intent = Intent(this, Signupscreen::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        hideKeyboard()

        if (validateInputs(email, password)) {
            performLoginSimulation()
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            emailLayout.error = getString(R.string.email_required)
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = getString(R.string.invalid_email)
            isValid = false
        }

        if (password.isEmpty()) {
            passwordLayout.error = getString(R.string.password_required)
            isValid = false
        } else if (password.length < 6) {
            passwordLayout.error = getString(R.string.invalid_password)
            isValid = false
        }

        return isValid
    }

    private fun performLoginSimulation() {
        // Disable button and show progress for "Authentic" feel
        loginButton.isEnabled = false
        loginButton.text = getString(R.string.logging_in)

        // Simulate network delay
        Handler(Looper.getMainLooper()).postDelayed({
            loginButton.isEnabled = true
            loginButton.text = getString(R.string.login_button)

            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            
            val intent = Intent(this, Dashboardscreen::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}