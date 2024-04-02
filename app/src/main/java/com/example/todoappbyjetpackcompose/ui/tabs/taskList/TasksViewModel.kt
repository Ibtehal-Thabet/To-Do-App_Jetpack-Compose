package com.example.todoappbyjetpackcompose.ui.tabs.taskList

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.MyDataBase
import com.example.todo.model.Task
import com.example.todoappbyjetpackcompose.todoDatabase.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val taskRepository = TaskRepository(MyDataBase.getInstance(application))

    var taskItems = taskRepository.taskItems

    private val _taskFlow = MutableStateFlow<List<Task>>(listOf())
    val taskFlow get() = _taskFlow.asStateFlow()

    var taskItem by mutableStateOf(Task(0, "", "", 0, false))
        private set

    fun addTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.addTask(task)
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.deleteTask(task)
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.updateTask(task)
            }
        }
    }

    fun getTasksByDay(dateTime: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _taskFlow.value = taskRepository.getTasksByDay(dateTime)
            }
        }
    }


    fun getTasksById(id: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskRepository.getTaskById(id)
            }
        }
    }

    fun completedTask(task: Task, id: Int, isDone: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                taskItem = task.copy(id = task.id, isDone = isDone)
                updateTask(task = task.copy(id = task.id, isDone = isDone))
            }
        }

    }
}