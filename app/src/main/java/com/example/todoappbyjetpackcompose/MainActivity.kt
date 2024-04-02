package com.example.todoappbyjetpackcompose

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todo.model.Task
import com.example.todoappbyjetpackcompose.ui.BottomNavigationItem
import com.example.todoappbyjetpackcompose.ui.screens.Screens
import com.example.todoappbyjetpackcompose.ui.tabs.taskList.TasksViewModel
import com.example.todoappbyjetpackcompose.ui.screens.AddTask
import com.example.todoappbyjetpackcompose.ui.tabs.Settings
import com.example.todoappbyjetpackcompose.ui.tabs.isLangChanged
import com.example.todoappbyjetpackcompose.ui.tabs.langIndex
import com.example.todoappbyjetpackcompose.ui.tabs.taskList.TasksList
import com.example.todoappbyjetpackcompose.ui.tabs.taskList.dateTime
import com.example.todoappbyjetpackcompose.ui.theme.LightPrimary
import com.example.todoappbyjetpackcompose.ui.theme.TodoAppByJetpackComposeTheme

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel by viewModels<TasksViewModel>()
            val tasksList by viewModel.taskFlow.collectAsState(initial = listOf())
            LaunchedEffect(key1 = Unit) {
                viewModel.getTasksByDay(dateTime)
            }
            TodoAppByJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(tasksList = tasksList,
                        viewModel = viewModel,
                        onAddTaskClick = { task -> viewModel.addTask(task) },
                        onDoneTaskClick = { task ->
                            viewModel.completedTask(
                                task = task,
                                id = task.id ?: 0,
                                isDone = true
                            )
                        },
                        onDateClickListener = { dateTime -> viewModel.getTasksByDay(dateTime) },
                        navToEditActivity = {})
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(
    tasksList: List<Task>,
    viewModel: TasksViewModel,
    onAddTaskClick: (Task) -> Unit,
    onDoneTaskClick: (Task) -> Unit,
    onDateClickListener: (Long) -> Unit,
    navToEditActivity: (Task) -> Unit
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    var selectedScreen by remember {
        mutableStateOf(0)
    }
//    val screens = listOf("Tasks List", "Settings")
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxWidth(),

        bottomBar = {
            Box {
                NavigationBar(
                    modifier = Modifier
                        .height(75.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .padding(0.dp),
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                ) {
                    BottomNavigationItem().bottomNavigationItems()
                        .forEachIndexed { index, navigationItem ->

                            NavigationBarItem(
                                selected = selectedScreen == index,
                                onClick = {
                                    selectedScreen = index
                                    navController.navigate(navigationItem.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        navigationItem.icon,
                                        contentDescription = navigationItem.label,
                                        modifier = Modifier
                                            .padding(10.dp),
                                        tint = if (selectedScreen == index) LightPrimary
                                        else Color.Gray
                                    )
                                },
                                colors = NavigationBarItemDefaults
                                    .colors(
                                        selectedIconColor = LightPrimary,
                                        indicatorColor = MaterialTheme.colorScheme.onSecondary
                                    )
                            )
                        }
                }

                FloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                    },
                    containerColor = LightPrimary,
                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    shape = CircleShape,
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.Center),

                    ) {
                    Icon(Icons.Filled.Add, "Add Task")

                    if (showBottomSheet) {
                        ModalBottomSheet(
//                        modifier = Modifier.height(75.dp),
                            modifier = Modifier.fillMaxHeight(0.8f),
                            onDismissRequest = {
                                showBottomSheet = false
                            },
                            windowInsets = WindowInsets.ime,
                            sheetState = sheetState,
                            containerColor = MaterialTheme.colorScheme.onSecondary,
                            dragHandle = {
                                BottomSheetDefaults.DragHandle()
                            },
                        ) {
                            // Sheet content
                            AddTask(onAddTaskClick)
                            Spacer(modifier = Modifier.padding(top = 100.dp))

//                            Button(onClick = {
//                                scope.launch { sheetState.hide() }.invokeOnCompletion {
//                                    if (!sheetState.isVisible) {
//                                        showBottomSheet = false
//                                    }
//                                }
//                            },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(top = 2.dp, start = 12.dp, end = 12.dp),
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = LightPrimary,
//                                    contentColor = Color.White
//                                )){
////                            Text("Hide bottom sheet")
//                            }
                        }
                    }
                }
            }
        },

        ) { contentPadding ->

        NavHost(
            navController = navController,
            startDestination = Screens.TasksList.screen,
            modifier = Modifier.padding(paddingValues = contentPadding)
        ) {
            composable(Screens.TasksList.screen) {
                TasksList(
                    navController = navController, tasksList, onDoneTaskClick,
                    navToEditActivity, onDateClickListener, viewModel
                )
            }
            composable(Screens.Settings.screen) {
                Settings(navController = navController)
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    TodoAppByJetpackComposeTheme {
//        ScaffoldFunMain(listOf(), {})
    }
}