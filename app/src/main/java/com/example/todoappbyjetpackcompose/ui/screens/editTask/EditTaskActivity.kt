package com.example.todoappbyjetpackcompose.ui.screens.editTask

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.model.Task
import com.example.todoappbyjetpackcompose.R
import com.example.todoappbyjetpackcompose.ui.screens.calendar
import com.example.todoappbyjetpackcompose.ui.tabs.taskList.TasksViewModel
import com.example.todoappbyjetpackcompose.ui.theme.LightPrimary
import com.example.todoappbyjetpackcompose.ui.theme.TodoAppByJetpackComposeTheme
import com.example.todoappbyjetpackcompose.utils.TopAppBar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATION")
@RequiresApi(Build.VERSION_CODES.O)
class EditTaskActivity : ComponentActivity() {

    private val task: Task by lazy {
        intent?.getSerializableExtra(TASK_ID) as Task
    }
    val viewModel by viewModels<TasksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppByJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EditTaskContent(
                        task = task,
                        onUpdateTaskClick = { task -> viewModel.updateTask(task) }) {
                        finish()
                    }
                }
            }
        }
    }

    companion object {
        private const val TASK_ID = "task_id"
        fun newIntent(context: Context, task: Task) =
            Intent(context, EditTaskActivity::class.java).apply {
                putExtra(TASK_ID, task)
            }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditTaskContent(
    task: Task,
    onUpdateTaskClick: (Task) -> Unit,
    onFinish: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(stringResource(id = R.string.app_title)) {
                onFinish()
            }
            EditTaskCard(task = task, taskId = task.id ?: 0, onUpdateTaskClick = onUpdateTaskClick)
        },
    ) {


    }
}

@Composable
fun EditTaskCard(task: Task, taskId: Int, onUpdateTaskClick: (Task) -> Unit) {

    val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var dateTime by remember { mutableStateOf(simpleDateFormat.format(task.dateTime)) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, bottom = 50.dp, start = 30.dp, end = 30.dp)
            .clip(RoundedCornerShape(15.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {

            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.edit_task),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )

            TextField(
                value = title ?: "",
                onValueChange = {
                    title = it
                    task.title = title
                },
                readOnly = false,
                singleLine = true,
                label = {
                    Text(
                        stringResource(id = R.string.task_title)
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    //                textColor = Color.Black,
                    focusedBorderColor = Color.Blue, // Custom focused border color
                    unfocusedBorderColor = Color.Gray, // Custom unfocused border color
                ),
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )

            TextField(
                value = description ?: "",
                onValueChange = {
                    description = it
                    task.description = description
                },
                singleLine = false,
                label = {
                    Text(
                        stringResource(id = R.string.task_desc)
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                colors = OutlinedTextFieldDefaults.colors(
                    //                textColor = Color.Black,
                    focusedBorderColor = Color.Blue, // Custom focused border color
                    unfocusedBorderColor = Color.Gray, // Custom unfocused border color
                ),
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .fillMaxWidth(),
            )

            val context = LocalContext.current

            Text(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable(enabled = true) {
                        val dialog = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            DatePickerDialog(context)
                        } else {
                            TODO("VERSION.SDK_INT < N")
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            dialog.setOnDateSetListener { datePicker, year, month, day ->
                                dateTime = "$year-${month + 1}-$day"

                                calendar.set(year, month, day)
                                //to ignore time
                                calendar.set(Calendar.HOUR_OF_DAY, 0)
                                calendar.set(Calendar.MINUTE, 0)
                                calendar.set(Calendar.SECOND, 0)
                                calendar.set(Calendar.MILLISECOND, 0)
                            }
                        }
                        dialog.show()
                    },
                text = stringResource(id = R.string.select_date),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )


            Text(
                text = dateTime,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 140.dp, bottom = 12.dp)
            )


            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Button(
                onClick = {
                    if (title!!.isNotBlank() && description!!.isNotBlank() && dateTime.toString()
                            .isNotBlank()
                    ) {
                        val task =
                            Task(
                                id = taskId,
                                title = title,
                                description = description,
                                dateTime = calendar.timeInMillis
                            )
                        onUpdateTaskClick(task)
                        Toast.makeText(context, "Task Updated Successfully", Toast.LENGTH_LONG)
                            .show()

                    } else if (title!!.isBlank()) {
                        Toast.makeText(context, "Please enter Title", Toast.LENGTH_LONG).show()
                    } else if (description!!.isBlank()) {
                        Toast.makeText(context, "Please enter Description", Toast.LENGTH_LONG)
                            .show()
                    }


                },
                modifier = Modifier
                    .width(255.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp, top = 12.dp, start = 12.dp, end = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightPrimary,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(id = R.string.save_task_changes),
                    fontSize = 16.sp,
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun TriggerEvent(
    event: EditTaskEvent,
    onFinish: () -> Unit
) {
    when (event) {
        EditTaskEvent.NavigateBack -> {
            onFinish()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        EditTaskContent(Task(), {}, {})
    }

}