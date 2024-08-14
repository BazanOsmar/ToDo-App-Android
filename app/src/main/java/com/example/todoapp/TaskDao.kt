package com.example.todoapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM task_table")
    fun getAllTask(): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :id")
    suspend fun getById(id: Int): Task
    @Insert
    suspend fun insert(task: Task)
    @Delete
    suspend fun deleteTask(tarea: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("DELETE FROM task_table WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("UPDATE task_table SET titleTask = :titleTask, description = :description, type = :type, date = :date WHERE id = :id")
    suspend fun updateTaskById(id: Int, titleTask: String, description: String, type: String, date: String)

    @Query("UPDATE task_table SET state = :state WHERE id = :id")
    suspend fun completedTaskById(id: Int, state: Boolean)
}