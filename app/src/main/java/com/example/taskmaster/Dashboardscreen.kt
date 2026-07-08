package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Dashboardscreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var emptyStateText: TextView
    private lateinit var auth: FirebaseAuth
    
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        auth = Firebase.auth
        
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboardscreen)

        initViews()
        setupSearchAndFilters()
        updateWelcomeMessage()
        observeViewModel()
    }

    private fun observeViewModel() {
        // Observe Task List reactively
        viewModel.taskList.observe(this) { tasks ->
            val selectedCategory = getSelectedCategory()
            val filteredTasks = if (selectedCategory == "All") {
                tasks
            } else {
                tasks.filter { it.category == selectedCategory }
            }
            
            taskAdapter.submitList(filteredTasks)
            emptyStateText.visibility = if (filteredTasks.isEmpty()) View.VISIBLE else View.GONE
        }

        // Dynamic Progress Monitor Calculation
        val progressStats = findViewById<TextView>(R.id.progress_stats)
        val progressBar = findViewById<LinearProgressIndicator>(R.id.progress_bar)
        
        viewModel.progressState.observe(this) { progress ->
            progressStats.text = getString(
                R.string.progress_stats_format, 
                progress.completed, 
                progress.total, 
                progress.percentage,
            )
            progressBar.setProgress(progress.percentage, true)
        }
    }

    private fun updateWelcomeMessage() {
        val currentUser = auth.currentUser
        val welcomeText = findViewById<TextView>(R.id.welcome_user_text)
        if ((currentUser != null) && (welcomeText != null)) {
            val name = currentUser.displayName ?: currentUser.email?.split("@")?.get(0) ?: "User"
            welcomeText.text = getString(R.string.welcome_user_greeting, name)
        }
    }

    private fun initViews() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.tasks_recycler_view)
        emptyStateText = findViewById(R.id.empty_state_text)
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter { task ->
            viewModel.toggleTaskCompletion(task)
        }
        recyclerView.adapter = taskAdapter

        findViewById<ExtendedFloatingActionButton>(R.id.fab_add_task).setOnClickListener {
            startActivity(Intent(this, Taskcreationscreen::class.java))
        }

        findViewById<View>(R.id.board_icon).setOnClickListener {
            startActivity(Intent(this, ProjectBoardScreen::class.java))
        }

        findViewById<View>(R.id.test_interaction_icon).setOnClickListener {
            startActivity(Intent(this, InteractionTestActivity::class.java))
        }

        // Logout functionality
        findViewById<View>(R.id.profile_icon).setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Authenticationscreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setupSearchAndFilters() {
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false
                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.setSearchQuery(newText ?: "")
                    return true
                }
            },
        )

        findViewById<ChipGroup>(R.id.filter_chip_group).setOnCheckedStateChangeListener { _, _ ->
            viewModel.taskList.value?.let { tasks ->
                val selectedCategory = getSelectedCategory()
                val filteredTasks = if (selectedCategory == "All") {
                    tasks
                } else {
                    tasks.filter { it.category == selectedCategory }
                }
                taskAdapter.submitList(filteredTasks)
                emptyStateText.visibility = if (filteredTasks.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun getSelectedCategory(): String {
        val chipGroup = findViewById<ChipGroup>(R.id.filter_chip_group)
        return when (chipGroup.checkedChipId) {
            R.id.chip_work -> "Work"
            R.id.chip_personal -> "Personal"
            R.id.chip_health -> "Health"
            else -> "All"
        }
    }
}
