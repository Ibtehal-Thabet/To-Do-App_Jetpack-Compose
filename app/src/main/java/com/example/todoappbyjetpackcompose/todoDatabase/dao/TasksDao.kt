package com.example.todo.dao

import androidx.room.*
import com.example.todo.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Insert
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("select * from tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("select * from tasks where dateTime = :dateTime")
    fun getTasksByDay(dateTime: Long): Flow<List<Task>>

    @Query("select * from tasks where id = :id")
    fun getTaskById(id: Int): Task
}