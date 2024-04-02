package com.example.todoappbyjetpackcompose.ui.tabs.taskList

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todo.model.Task
import com.example.todoappbyjetpackcompose.R
import com.example.todoappbyjetpackcompose.ui.screens.editTask.EditTaskActivity
import com.example.todoappbyjetpackcompose.ui.tabs.langIndex
import com.example.todoappbyjetpackcompose.ui.theme.IsDoneColor
import com.example.todoappbyjetpackcompose.ui.theme.LightPrimary
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskList(
    tasksList: List<Task>,
    viewModel: TasksViewModel,
    onDoneTaskClick: (Task) -> Unit,
    navToEditActivity: (Task) -> Unit,
    navController: NavController?
) {
    LazyColumn {
        items(items = tasksList,
            key = { it.id!! }) { task ->

            SwipeToDeleteContainer(
                item = task,
                onDelete = {
                    deleteTaskFromDatabase(task, viewModel = viewModel)
                }) { task ->

                TaskCard(
                    task = task, viewModel,
                    onDoneTaskClick = onDoneTaskClick,
                    navToEditActivity = navToEditActivity,
                    navController
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskCard(
    task: Task,
    viewModel: TasksViewModel,
    onDoneTaskClick: (Task) -> Unit,
    navToEditActivity: (Task) -> Unit,
    navController: NavController?
) {

    val context = LocalContext.current
    var isTaskDone by rememberSaveable {
        mutableStateOf(task.isDone)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .padding(start = 12.dp, end = 12.dp)
            .clickable {
                context.startActivity(EditTaskActivity.newIntent(context, task = task))
                navToEditActivity(task)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .height(IntrinsicSize.Min)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.dp),
                color = if (isTaskDone) IsDoneColor
                else LightPrimary
            )

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(3.dp)
            )
            {
                Text(
                    modifier = Modifier.padding(top = 4.dp, start = 5.dp),
                    text = task.title ?: "",
                    style = TextStyle(
                        color = if (isTaskDone) IsDoneColor
                        else LightPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    modifier = Modifier.padding(start = 5.dp, 4.dp),
                    text = task.description ?: "",
                    style = TextStyle(color = MaterialTheme.colorScheme.onPrimary, fontSize = 14.sp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {

                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = "Date Image",
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .align(Alignment.CenterVertically)
                            .size(16.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )

                    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = if (task.dateTime != null) simpleDateFormat.format(task.dateTime)
                        else "",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp
                        )
                    )
                }

            }

            if (isTaskDone) {
                Text(
                    text = stringResource(id = R.string.done),
                    style = TextStyle(
                        color = IsDoneColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .width(50.dp)
                        .align(Alignment.CenterVertically),
                )
            } else {
                Button(
                    onClick = {
                        isTaskDone = true
                        onDoneTaskClick(task)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .width(50.dp)
                        .align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
//                    if (task.isDone) Color.Transparent
//                        else
                        LightPrimary,
                        contentColor = Color.White
                    ),

                    ) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = "Task done",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToEnd) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        modifier = Modifier
            .clip(RoundedCornerShape(48.dp))
            .shadow(elevation = 0.dp, shape = RoundedCornerShape(18.dp))
            .padding(12.dp),
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(state = state,
            backgroundContent = fun RowScope.() {
                DeleteTaskBackground(swipeDismissState = state)
            }, directions = setOf(DismissDirection.StartToEnd),
            content = fun RowScope.() {
                content(item)
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteTaskBackground(swipeDismissState: DismissState) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.StartToEnd) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {

        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }

}

@Preview(name = "Tasks Card", showSystemUi = true)
@Composable
fun TasksCardPreview() {
//    TaskCard(task = Task(title = "Task title", description = "Task Description", isDone = true),)
}

private fun deleteTaskFromDatabase(task: Task, viewModel: TasksViewModel) {
    viewModel.deleteTask(task)
}
