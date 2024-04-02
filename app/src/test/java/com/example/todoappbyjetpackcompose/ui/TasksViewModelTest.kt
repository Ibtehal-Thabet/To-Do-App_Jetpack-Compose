package com.example.todoappbyjetpackcompose.ui

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todo.MyDataBase
import com.example.todo.model.Task
import com.example.todoappbyjetpackcompose.ui.tabs.taskList.TasksViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
//import org.junit.jupiter.api.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    private lateinit var tasksViewModel: TasksViewModel
    private lateinit var taskDatabase: MyDataBase

    private lateinit var appContext: Application


    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() = runBlocking {
        // We initialise the reminders to 3
        appContext = getApplicationContext()
        tasksViewModel = TasksViewModel(appContext)
    }


    @Test
    fun `insert completed task`() = runBlocking {
        val task = Task(1, "task 1", "desc 1", 0, true)
        tasksViewModel.addTask(task)
        val result = tasksViewModel.taskItems.first()

        assertThat(result, `is`(1))
//        Assert.assertEquals(1, result.size)
//        Assert.assertEquals("task 1", result[0].title)
//        Assert.assertEquals(true, result[0].isDone)
    }

    @Test
    fun `insert uncompleted task`() = runBlocking {
        val task = Task(1, "task 1", "desc 1", 0, false)
        tasksViewModel.addTask(task)
        val result =
            tasksViewModel.taskItems.first()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("task 1", result[0].title)
        Assert.assertEquals(false, `is`(result[0].isDone))
    }

    @Test
    fun `insert more than task`() = runBlocking {
        val task1 = Task(1, "task 1", "desc 1", 0, false)
        val task2 = Task(2, "task 2", "desc 2", 1, true)
        val task3 = Task(3, "task 3", "desc 3", 2, false)

        tasksViewModel.addTask(task1)
        tasksViewModel.addTask(task2)
        tasksViewModel.addTask(task3)
        val result =
            tasksViewModel.taskItems.first()

        Assert.assertEquals(3, result.size)
        Assert.assertEquals("desc 2", result[1].title)
        Assert.assertEquals(false, result[2].isDone)
    }

    @Test
    fun `delete task`() = runBlocking {
        val task = Task(1, "task 1", "desc 1", 0, false)
        tasksViewModel.addTask(task)
        tasksViewModel.deleteTask(task)
        val result =
            tasksViewModel.taskItems.first()
        Assert.assertEquals(0, result.size)
    }


//    @After
//    fun tearDown() {
//        taskDatabase.close()
//    }
}