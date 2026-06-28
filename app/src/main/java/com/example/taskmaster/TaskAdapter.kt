package com.example.taskmaster

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onTaskChecked: (Task) -> Unit,
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(TaskDiffCallback()) {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.task_title)
        val category: TextView = view.findViewById(R.id.task_category)
        val time: TextView = view.findViewById(R.id.task_time)
        val date: TextView = view.findViewById(R.id.task_date)
        val indicator: View = view.findViewById(R.id.priority_indicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        holder.title.text = task.title
        holder.category.text = task.category
        holder.time.text = task.time
        holder.date.text = task.date
        
        // Strike-through if completed
        holder.title.paint.isStrikeThruText = task.isCompleted
        holder.title.alpha = if (task.isCompleted) 0.5f else 1.0f

        val indicatorColor = when(task.category) {
            "Work" -> 0xFF6200EE.toInt()
            "Personal" -> 0xFF03DAC5.toInt()
            "Health" -> 0xFFFF9800.toInt()
            else -> 0xFF9C27B0.toInt()
        }
        holder.indicator.setBackgroundColor(indicatorColor)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Taskdetailview::class.java).apply {
                putExtra("TASK_ID", task.id)
                putExtra("TASK_TITLE", task.title)
                putExtra("TASK_CATEGORY", task.category)
                putExtra("TASK_TIME", task.time)
                putExtra("TASK_DESC", task.description)
                putExtra("TASK_DATE", task.date)
                putExtra("TASK_COMPLETED", task.isCompleted)
            }
            context.startActivity(intent)
        }
        
        holder.itemView.setOnLongClickListener {
            onTaskChecked(task)
            true
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem
    }
}
