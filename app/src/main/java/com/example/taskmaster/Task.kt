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
    val imagePath: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isCompleted: Boolean = false,
)
