package com.example.taskmaster

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class Dashboardscreen : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var emptyStateText: TextView
    
    private val fullTaskList = listOf(
        Task("Finish Project Proposal", "Work", "10:00 AM"),
        Task("Grocery Shopping", "Personal", "02:30 PM"),
        Task("Team Sync Meeting", "Work", "04:00 PM"),
        Task("Evening Workout", "Health", "06:00 PM"),
        Task("Read 20 pages", "Education", "09:00 PM")
    )
    
    private var filteredList = fullTaskList.toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboardscreen)

        initViews()
        setupSearchAndFilters()
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
        taskAdapter = TaskAdapter(filteredList)
        recyclerView.adapter = taskAdapter

        findViewById<ExtendedFloatingActionButton>(R.id.fab_add_task).setOnClickListener {
            startActivity(Intent(this, Taskcreationscreen::class.java))
        }

        findViewById<View>(R.id.board_icon).setOnClickListener {
            startActivity(Intent(this, ProjectBoardScreen::class.java))
        }
    }

    private fun setupSearchAndFilters() {
        // 1. Search Logic
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                filterTasks(newText, getSelectedCategory())
                return true
            }
        })

        // 2. Chip Filter Logic
        findViewById<ChipGroup>(R.id.filter_chip_group).setOnCheckedStateChangeListener { _, checkedIds ->
            val category = when (checkedIds.firstOrNull()) {
                R.id.chip_work -> "Work"
                R.id.chip_personal -> "Personal"
                R.id.chip_health -> "Health"
                else -> "All"
            }
            filterTasks(searchView.query.toString(), category)
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

    private fun filterTasks(query: String?, category: String) {
        val searchQuery = query?.lowercase() ?: ""
        
        filteredList.clear()
        val results = fullTaskList.filter { task ->
            val matchesQuery = task.title.lowercase().contains(searchQuery)
            val matchesCategory = category == "All" || task.category == category
            matchesQuery && matchesCategory
        }
        filteredList.addAll(results)
        taskAdapter.notifyDataSetChanged()
        
        emptyStateText.visibility = if (filteredList.isEmpty()) View.VISIBLE else View.GONE
    }
}

// Data Model
data class Task(val title: String, val category: String, val time: String)

// Professional Task Adapter
class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.task_title)
        val category: TextView = view.findViewById(R.id.task_category)
        val time: TextView = view.findViewById(R.id.task_time)
        val indicator: View = view.findViewById(R.id.priority_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.title.text = task.title
        holder.category.text = task.category
        holder.time.text = task.time
        
        val indicatorColor = when(task.category) {
            "Work" -> 0xFF6200EE.toInt()
            "Personal" -> 0xFF03DAC5.toInt()
            "Health" -> 0xFFFF9800.toInt()
            else -> 0xFF9C27B0.toInt()
        }
        holder.indicator.setBackgroundColor(indicatorColor)

        // Navigate to Task Detail View
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Taskdetailview::class.java).apply {
                putExtra("TASK_TITLE", task.title)
                putExtra("TASK_CATEGORY", task.category)
                putExtra("TASK_TIME", task.time)
                // Passing dummy description and date for now
                putExtra("TASK_DESC", "This is a detailed description for ${task.title}. It requires focus and dedication to complete on time.")
                putExtra("TASK_DATE", "Today, 25 Oct")
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = tasks.size
}