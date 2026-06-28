package com.example.taskmaster

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val time: String,
    val description: String = "",
    val date: String = "",
    val isCompleted: Boolean = false
)
