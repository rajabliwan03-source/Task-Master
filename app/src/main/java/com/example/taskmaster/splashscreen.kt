package com.example.taskmaster

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@SuppressLint("CustomSplashScreen")
class Splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        NotificationHelper.initNotificationChannel(this)
        
        enableEdgeToEdge()
        setContentView(R.layout.activity_splashscreen)

        val mainView = findViewById<View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Animate the logo container for a professional feel
        val logoContainer = findViewById<View>(R.id.logo_container)
        if (logoContainer != null) {
            val fadeIn = AlphaAnimation(0f, 1f).apply {
                duration = 1200
                fillAfter = true
            }
            logoContainer.startAnimation(fadeIn)
        }

        // Delayed navigation check
        Handler(Looper.getMainLooper()).postDelayed(
            {
                checkAuthAndNavigate()
            },
            2500,
        )
    }

    private fun checkAuthAndNavigate() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in, go to Dashboard
            val intent = Intent(this, Dashboardscreen::class.java)
            startActivity(intent)
        } else {
            // No user is signed in, go to Auth Screen
            val intent = Intent(this, Authenticationscreen::class.java)
            startActivity(intent)
        }
        
        // Handle activity transition based on API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                android.R.anim.fade_in,
                android.R.anim.fade_out,
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        
        finish()
    }
}
