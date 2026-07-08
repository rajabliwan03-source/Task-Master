package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ProjectBoardScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_projectboardscreen)

        setupEdgeToEdge()
        setupToolbar()
        setupBoards()
        setupFab()
    }

    private fun setupEdgeToEdge() {
        val mainView = findViewById<android.view.View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun setupBoards() {
        // Dummy Data for Columns - Updated to use the new Task constructor
        val todoTasks = listOf(
            Task(title = "Research Competitors", category = "Work", time = "09:00 AM"),
            Task(title = "Draft Design Docs", category = "Work", time = "11:30 AM"),
        )
        val inProgressTasks = listOf(
            Task(title = "Develop Auth Module", category = "Work", time = "02:00 PM")
        )
        val doneTasks = listOf(
            Task(title = "Setup Project Repository", category = "Work", time = "Done"),
            Task(title = "Initial Meeting", category = "Work", time = "Done")
        )

        setupRecyclerView(findViewById(R.id.todo_recycler_view), todoTasks)
        setupRecyclerView(findViewById(R.id.inprogress_recycler_view), inProgressTasks)
        setupRecyclerView(findViewById(R.id.done_recycler_view), doneTasks)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, tasks: List<Task>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = TaskAdapter { /* No action needed for board view currently */ }
        adapter.submitList(tasks)
        recyclerView.adapter = adapter
    }

    private fun setupFab() {
        findViewById<ExtendedFloatingActionButton>(R.id.fab_add_task).setOnClickListener {
            startActivity(Intent(this, Taskcreationscreen::class.java))
        }
    }
}
