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

    private fun setupBoards() {
        // Dummy Data for Columns
        val todoTasks = listOf(
            Task("Research Competitors", "Work", "09:00 AM"),
            Task("Draft Design Docs", "Work", "11:30 AM")
        )
        val inProgressTasks = listOf(
            Task("Develop Auth Module", "Work", "02:00 PM")
        )
        val doneTasks = listOf(
            Task("Setup Project Repository", "Work", "Done"),
            Task("Initial Meeting", "Work", "Done")
        )

        setupRecyclerView(findViewById(R.id.todo_recycler_view), todoTasks)
        setupRecyclerView(findViewById(R.id.inprogress_recycler_view), inProgressTasks)
        setupRecyclerView(findViewById(R.id.done_recycler_view), doneTasks)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, tasks: List<Task>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TaskAdapter(tasks)
    }

    private fun setupFab() {
        findViewById<ExtendedFloatingActionButton>(R.id.fab_add_task).setOnClickListener {
            startActivity(Intent(this, Taskcreationscreen::class.java))
        }
    }
}