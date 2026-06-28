package com.example.taskmaster

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    val totalCount: Flow<Int> = taskDao.getTotalCount()
    val completedCount: Flow<Int> = taskDao.getCompletedCount()

    fun searchTasks(query: String): Flow<List<Task>> {
        return taskDao.searchTasks(query)
    }

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }
}
