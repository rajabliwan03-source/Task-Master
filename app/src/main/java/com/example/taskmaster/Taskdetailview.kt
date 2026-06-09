package com.example.taskmaster

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip

class Taskdetailview : AppCompatActivity() {

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
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun loadTaskDetails() {
        // Retrieve data from Intent extras
        val title = intent.getStringExtra("TASK_TITLE") ?: "No Title"
        val category = intent.getStringExtra("TASK_CATEGORY") ?: "General"
        val time = intent.getStringExtra("TASK_TIME") ?: "Not set"
        val date = intent.getStringExtra("TASK_DATE") ?: "Today"
        val desc = intent.getStringExtra("TASK_DESC") ?: "No description provided."

        // Update UI
        findViewById<TextView>(R.id.detail_task_title).text = title
        findViewById<Chip>(R.id.detail_task_category).text = category
        findViewById<TextView>(R.id.detail_task_time).text = time
        findViewById<TextView>(R.id.detail_task_date).text = date
        findViewById<TextView>(R.id.detail_task_desc).text = desc

        // Set priority indicator color based on category
        val indicatorColor = when(category) {
            "Work" -> 0xFF6200EE.toInt()
            "Personal" -> 0xFF03DAC5.toInt()
            "Health" -> 0xFFFF9800.toInt()
            else -> 0xFF9C27B0.toInt()
        }
        findViewById<View>(R.id.priority_indicator).setBackgroundColor(indicatorColor)
    }

    private fun setupActionButtons() {
        findViewById<MaterialButton>(R.id.complete_button).setOnClickListener {
            Toast.makeText(this, "Task marked as completed!", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<MaterialButton>(R.id.edit_button).setOnClickListener {
            Toast.makeText(this, "Opening Edit Mode...", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialButton>(R.id.delete_button).setOnClickListener {
            Toast.makeText(this, "Task Deleted!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}