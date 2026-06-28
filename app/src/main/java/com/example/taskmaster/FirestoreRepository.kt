package com.example.taskmaster

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/**
 * Production-ready Repository class for handling Firebase Firestore operations.
 */
class FirestoreRepository {

    private val db: FirebaseFirestore = Firebase.firestore
    private val TAG = "FirestoreRepository"

    /**
     * Saves a new task using the standard Firebase Listener pattern.
     * Use this if you prefer traditional callback handling.
     */
    fun saveTaskWithListeners(
        title: String,
        description: String,
        priority: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val task = TaskRemote(
            title = title,
            description = description,
            priority = priority
        )

        db.collection("tasks")
            .add(task)
            .addOnSuccessListener { ref ->
                Log.d(TAG, "Task saved (Listener): ${ref.id}")
                onSuccess(ref.id)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error saving task (Listener)", e)
                onFailure(e)
            }
    }

    /**
     * Saves a new task using Kotlin Coroutines (Suspend function).
     * This is the recommended "Optimal" way for modern Android development.
     */
    suspend fun saveTask(title: String, description: String, priority: String): Result<String> {
        return try {
            val task = TaskRemote(
                title = title,
                description = description,
                priority = priority
            )
            val documentReference = db.collection("tasks").add(task).await()
            Log.d(TAG, "Task saved (Coroutine): ${documentReference.id}")
            Result.success(documentReference.id)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving task (Coroutine)", e)
            Result.failure(e)
        }
    }
}
