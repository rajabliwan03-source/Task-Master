package com.example.taskmaster

import android.app.NotificationChannel
import android.util.Log
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * NotificationHelper: Manages the production-ready notification subsystem.
 * Handles Channel creation for Android 8.0+ and building standard notifications.
 */
object NotificationHelper {

    private const val CHANNEL_ID = "taskmaster_release_notifications"
    private const val CHANNEL_NAME = "Taskmaster System Updates"

    /**
     * Initializes the notification channel. Call this in Application.onCreate() 
     * or at the start of your main activity.
     */
    fun initNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notifies users about task operations and database updates."
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d("Taskmaster", "Notification Channel Registered: $CHANNEL_ID")
        }
    }

    /**
     * Triggers a system notification for a successful task creation.
     */
    fun showTaskSavedNotification(context: Context, taskTitle: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // System fallback info icon
            .setContentTitle("Task Successfully Logged")
            .setContentText("'$taskTitle' has been secured to local storage.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Dismisses when tapped

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
