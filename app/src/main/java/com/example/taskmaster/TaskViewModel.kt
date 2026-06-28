package com.example.taskmaster

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    
    private val _searchQuery = MutableStateFlow("")
    
    val taskList: LiveData<List<Task>>
    val progressState: LiveData<TaskProgress>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)

        taskList = _searchQuery
            .flatMapLatest { query ->
                if (query.isEmpty()) repository.allTasks else repository.searchTasks(query)
            }
            .asLiveData()

        progressState = combine(
            repository.totalCount,
            repository.completedCount,
        ) { total, completed ->
            val percentage = if (total > 0) ((completed.toFloat() / total) * 100).toInt() else 0
            TaskProgress(completed, total, percentage)
        }.asLiveData()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addTask(title: String, category: String, time: String, date: String, description: String) = viewModelScope.launch {
        repository.insert(Task(title = title, category = category, time = time, date = date, description = description))
    }

    fun toggleTaskCompletion(task: Task) = viewModelScope.launch {
        repository.update(task.copy(isCompleted = !task.isCompleted))
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }

    fun getTaskById(id: Int): LiveData<Task?> {
        // We can add a getTaskById to DAO if needed, or filter allTasks.
        // For simplicity, let's assume we pass the task ID and fetch it.
        // I'll add it to DAO/Repository for a proper architectural fix.
        return repository.allTasks.map { tasks -> tasks.find { it.id == id } }.asLiveData()
    }
}

data class TaskProgress(val completed: Int, val total: Int, val percentage: Int)
