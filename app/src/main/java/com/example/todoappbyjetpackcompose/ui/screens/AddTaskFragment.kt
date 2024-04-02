package com.example.todoappbyjetpackcompose.ui.screens

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.MyDataBase
import com.example.todo.model.Task
import com.example.todoappbyjetpackcompose.R
import com.example.todoappbyjetpackcompose.ui.theme.LightPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

val calendar = Calendar.getInstance()
var isAdded = false

@Composable
fun AddTask(onAddTaskClick: (Task) -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var task by remember {
        mutableStateOf(Task(title = title, description = description, isDone = false))
    }

    var isValidTitle by remember { mutableStateOf(false) }
    var isValidDesc by remember { mutableStateOf(false) }
    var isValidDate by remember { mutableStateOf(false) }

//    var isAdded by rememberSaveable {
//        mutableStateOf(false)
//    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {

        Text(
            modifier = Modifier
                .padding(12.dp)
                .align(CenterHorizontally),
            text = stringResource(id = R.string.add_task),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                isValidTitle = it.isNotEmpty() || it.isNotBlank()
            },
            singleLine = true,
            label = {
                Text(
                    stringResource(id = R.string.task_title)
                )
            },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = OutlinedTextFieldDefaults.colors(
                //                textColor = Color.Black,
                focusedBorderColor = LightPrimary, // Custom focused border color
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary, // Custom unfocused border color
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
//            isError = !isValidTitle
        )

        if (!isValidTitle) {
            Text(
                text = stringResource(id = R.string.enter_title),
                modifier = Modifier
                    .padding(start = 12.dp)
            )
        }

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
                isValidDesc = it.isNotEmpty() || it.isNotBlank()
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
                focusedBorderColor = LightPrimary, // Custom focused border color
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary, // Custom unfocused border color
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
//            isError = !isValidDesc
        )

        if (!isValidDesc) {
            Text(
                text = stringResource(id = R.string.enter_desc),
                modifier = Modifier
                    .padding(start = 12.dp)
            )
        }

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
                            date = "$year-${month + 1}-$day"

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
        )

        Text(
            text = date,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 140.dp, bottom = 12.dp)
        )

        Button(
            onClick = {
                if (title.isNotBlank() && description.isNotBlank() && date.isNotBlank()) {
                    isAdded = true
                    task =
                        Task(
                            title = title,
                            description = description,
                            dateTime = calendar.timeInMillis
                        )
                    onAddTaskClick(task)

                    Toast.makeText(context, R.string.successfully, Toast.LENGTH_LONG).show()
                } else if (title.isBlank()) {
                    Toast.makeText(context, R.string.enter_title, Toast.LENGTH_LONG).show()
                } else if (description.isBlank()) {
                    Toast.makeText(context, R.string.enter_desc, Toast.LENGTH_LONG).show()
                } else if (date.isBlank()) {
                    Toast.makeText(context, R.string.enter_date, Toast.LENGTH_LONG).show()
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 150.dp, top = 12.dp, start = 12.dp, end = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightPrimary,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(id = R.string.add_task))
        }
    }
}

@Preview(name = "Add Task", showSystemUi = true)
@Composable
fun AddTaskPreview() {
    AddTask({})
}

