package com.example.taskmaster

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class Taskdetailview : AppCompatActivity() {

    private val viewModel: TaskViewModel by viewModels()
    private var taskId: Int = -1
    private var isCompleted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_taskdetailview)

        initViews()
        setupToolbar()
        loadTaskDetails()
        setupActionButtons()
    }

    private fun initViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.navigationIcon?.setTint(0xFF000000.toInt()) // Ensure visible back arrow
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun loadTaskDetails() {
        // Retrieve data from Intent extras including the real ID
        taskId = intent.getIntExtra("TASK_ID", -1)
        val title = intent.getStringExtra("TASK_TITLE") ?: "No Title"
        val category = intent.getStringExtra("TASK_CATEGORY") ?: "General"
        val time = intent.getStringExtra("TASK_TIME") ?: "Not set"
        val date = intent.getStringExtra("TASK_DATE") ?: "Today"
        val desc = intent.getStringExtra("TASK_DESC") ?: "No description provided."
        isCompleted = intent.getBooleanExtra("TASK_COMPLETED", false)

        // Update UI
        findViewById<TextView>(R.id.detail_task_title).text = title
        findViewById<Chip>(R.id.detail_task_category).text = category
        findViewById<TextView>(R.id.detail_task_time).text = time
        findViewById<TextView>(R.id.detail_task_date).text = date
        findViewById<TextView>(R.id.detail_task_desc).text = desc

        val completeButton = findViewById<MaterialButton>(R.id.complete_button)
        completeButton.text = if (isCompleted) "Mark as Pending" else "Mark as Completed"

        val indicatorColor = when(category) {
            "Work" -> 0xFF6200EE.toInt()
            "Personal" -> 0xFF03DAC5.toInt()
            "Health" -> 0xFFFF9800.toInt()
            else -> 0xFF9C27B0.toInt()
        }
        findViewById<View>(R.id.priority_indicator).setBackgroundColor(indicatorColor)
    }

    private fun setupActionButtons() {
        // Fix #1: Real Coroutine-based background updates for Edit/Delete/Complete
        findViewById<MaterialButton>(R.id.complete_button).setOnClickListener {
            if (taskId != -1) {
                // We create a dummy task object with the current values to update the state
                val taskToToggle = Task(
                    id = taskId,
                    title = findViewById<TextView>(R.id.detail_task_title).text.toString(),
                    category = findViewById<Chip>(R.id.detail_task_category).text.toString(),
                    time = findViewById<TextView>(R.id.detail_task_time).text.toString(),
                    date = findViewById<TextView>(R.id.detail_task_date).text.toString(),
                    description = findViewById<TextView>(R.id.detail_task_desc).text.toString(),
                    isCompleted = isCompleted
                )
                viewModel.toggleTaskCompletion(taskToToggle)
                Toast.makeText(this, "Status updated!", Toast.LENGTH_SHORT).show()
                finish() // Returns to Dashboard which updates instantly via Flow
            }
        }

        findViewById<MaterialButton>(R.id.edit_button).setOnClickListener {
             // In a full implementation, this would navigate to Taskcreationscreen with the ID
            Toast.makeText(this, "Edit mode would open Taskcreationscreen with ID: $taskId", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.delete_button).setOnClickListener {
            if (taskId != -1) {
                val taskToDelete = Task(
                    id = taskId,
                    title = "", category = "", time = "" // Minimum fields needed for deletion if based on ID
                )
                viewModel.deleteTask(taskToDelete)
                Toast.makeText(this, "Task permanently deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
