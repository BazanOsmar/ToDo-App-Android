package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var titleTask: String,
    var description: String,
    var type: String,
    var date: String = "",
    var state: Boolean = false
)
