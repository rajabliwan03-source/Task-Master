package com.example.taskmaster

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.databinding.ActivityInteractionTestBinding
import kotlin.math.abs

/**
 * InteractionTestActivity: Handles UI framework lifecycle.
 * Delegates event routing and processing to InteractionHandler and logging to EventManager.
 */
class InteractionTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInteractionTestBinding
    private lateinit var eventManager: EventManager
    private lateinit var interactionHandler: InteractionHandler
    private lateinit var gestureDetector: GestureDetector

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInteractionTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Initialize decoupled OOP Components
        eventManager = EventManager()
        interactionHandler = InteractionHandler(binding.feedbackText, eventManager)

        // 2. Setup Keyboard Event Routing
        binding.inputField.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                interactionHandler.handleKeyboardInput(v.text.toString())
                true
            } else false
        }

        // 3. Configure GestureDetector and map raw events to semantic gestures
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            private val swipeThreshold = 100
            private val swipeVelocityThreshold = 100

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                interactionHandler.handleGesture("SINGLE_TAP")
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null) return false
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x

                if (abs(diffX) > abs(diffY)) {
                    // Horizontal Swipe
                    if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                        if (diffX > 0) interactionHandler.handleGesture("SWIPE_RIGHT")
                        else interactionHandler.handleGesture("SWIPE_LEFT")
                    }
                } else {
                    // Vertical Swipe
                    if (abs(diffY) > swipeThreshold && abs(velocityY) > swipeVelocityThreshold) {
                        if (diffY > 0) interactionHandler.handleGesture("SWIPE_DOWN")
                        else interactionHandler.handleGesture("SWIPE_UP")
                    }
                }
                return true
            }
        })

        // 4. Delegate Touch Events from the specific touch zone
        binding.touchZone.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }
}
