package com.example.taskmaster

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmaster.databinding.ActivityAddTaskBinding

/**
 * AddTaskActivity: Orchestrates task input, validation, and persistence.
 */
class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSaveTask.setOnClickListener {
            validateAndSave()
        }
    }

    private fun validateAndSave() {
        val title = binding.etTaskTitle.text.toString().trim()

        // 1. Strict Input Validation
        if (title.isEmpty()) {
            binding.etTaskTitle.error = "Title cannot be empty"
            return
        }

        // 2. Visual Feedback: Display Loading Overlay
        binding.loadingOverlay.visibility = View.VISIBLE

        // 3. Mock captured targets for Week 10 requirements
        val imageUriString = "content://media/external/images/mock_task.jpg"
        val calculatedLatitude = -1.2833
        val calculatedLongitude = 36.8167

        // 4. Secure write operation to SQLite
        val result = dbHelper.insertTask(
            title = title,
            desc = "No description provided",
            imagePath = imageUriString,
            lat = calculatedLatitude,
            lon = calculatedLongitude
        )

        // 5. Lifecycle Coordination
        if (result != -1L) {
            Toast.makeText(this, "Task Saved Successfully", Toast.LENGTH_SHORT).show()
            binding.loadingOverlay.visibility = View.GONE
            finish() // Cleanly exit activity
        } else {
            binding.loadingOverlay.visibility = View.GONE
            Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show()
        }
    }
}
