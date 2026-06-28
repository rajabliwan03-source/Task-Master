package com.example.taskmaster

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

/**
 * Production-ready Data Transfer Object (DTO) for Firebase Firestore.
 * Utilizes @ServerTimestamp to ensure the creation time is handled by the server.
 */
data class TaskRemote(
    val title: String = "",
    val description: String = "",
    val status: String = "Pending",
    val priority: String = "Medium",
    @ServerTimestamp val createdAt: Timestamp? = null
)
