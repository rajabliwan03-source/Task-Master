package com.example.taskmaster

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * EventManager: Responsible for formatting and outputting interaction logs.
 * Adheres to the Single Responsibility Principle.
 */
class EventManager {
    private val tag = "TaskMaster_Telemetry"
    private val timeFormatter = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    fun logInteraction(action: String, details: String) {
        val timestamp = timeFormatter.format(Date())
        Log.d(tag, "[$timestamp] ACTION: $action | DETAILS: $details")
    }
}
