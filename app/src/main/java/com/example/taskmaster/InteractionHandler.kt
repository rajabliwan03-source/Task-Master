package com.example.taskmaster

import android.widget.TextView

/**
 * InteractionHandler: Processes business logic for inputs and updates UI feedback.
 */
class InteractionHandler(
    private val feedbackDisplay: TextView,
    private val eventManager: EventManager
) {

    /**
     * Processes keyboard input from the EditText.
     */
    fun handleKeyboardInput(text: String) {
        val cleanText = text.trim()
        val feedback = "User typed: $cleanText"
        
        updateUI(feedback)
        eventManager.logInteraction("KEYBOARD", "Buffer: '$cleanText'")
    }

    /**
     * Processes various touch gestures (Tap, Swipe, etc.)
     */
    fun handleGesture(gestureType: String) {
        val feedback = "Gesture Detected: $gestureType"
        
        updateUI(feedback)
        eventManager.logInteraction("GESTURE", gestureType)
    }

    private fun updateUI(message: String) {
        feedbackDisplay.text = message
    }
}
