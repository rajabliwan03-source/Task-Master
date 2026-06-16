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

@SuppressLint("CustomSplashScreen")
class Splashscreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
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

        // Delayed navigation to Authentication Screen
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToAuth()
        }, 2500)
    }

    private fun navigateToAuth() {
        val intent = Intent(this, Authenticationscreen::class.java)
        startActivity(intent)
        
        // Handle activity transition based on API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        
        finish()
    }
}