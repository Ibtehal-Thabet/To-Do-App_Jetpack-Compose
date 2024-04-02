package com.example.todoappbyjetpackcompose.todoDatabase

import com.example.todo.MyDataBase
import com.example.todo.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepository(private val myDataBase: MyDataBase) {

    suspend fun addTask(taskItem: Task) {
        withContext(Dispatchers.IO) {
            myDataBase.tasksDao().insertTask(taskItem)
        }
    }

    suspend fun deleteTask(taskItem: Task) {
        withContext(Dispatchers.IO) {
            myDataBase.tasksDao().deleteTask(taskItem)
        }
    }

    suspend fun updateTask(taskItem: Task) {
        withContext(Dispatchers.IO) {
            myDataBase.tasksDao().updateTask(taskItem)
        }
    }

    suspend fun getTasksByDay(dateTime: Long) =
        withContext(Dispatchers.IO) {
            myDataBase.tasksDao().getTasksByDay(dateTime)
        }


    suspend fun getTaskById(id: Int) {
        withContext(Dispatchers.IO) {
            myDataBase.tasksDao().getTaskById(id)
        }
    }

    val taskItems: Flow<List<Task>> = myDataBase.tasksDao().getAllTasks()

}