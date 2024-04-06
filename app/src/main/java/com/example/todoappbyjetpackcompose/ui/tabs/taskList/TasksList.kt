package com.example.todoappbyjetpackcompose.ui.tabs.taskList

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todo.model.Task
import com.example.todoappbyjetpackcompose.R
import com.example.todoappbyjetpackcompose.calendar.CalendarDataSource
import com.example.todoappbyjetpackcompose.calendar.CalendarUiModel
import com.example.todoappbyjetpackcompose.ui.screens.isAdded
import com.example.todoappbyjetpackcompose.ui.tabs.langIndex
import com.example.todoappbyjetpackcompose.ui.theme.LightPrimary
import com.example.todoappbyjetpackcompose.utils.TopAppBar
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
val dataSource = CalendarDataSource()

@RequiresApi(Build.VERSION_CODES.O)
var dateTime: Long =
    dataSource.today.atStartOfDay(ZoneOffset.systemDefault()).toInstant().toEpochMilli()

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksList(
    navController: NavController? = null,
    tasksList: List<Task>,
    onDoneTaskClick: (Task) -> Unit,
    navToEditActivity: (Task) -> Unit,
    onDateClickListener: (Long) -> Unit,
    viewModel: TasksViewModel
) {

    Box(modifier = Modifier.fillMaxSize()) {
        TasksListContent(
            tasksList, viewModel, onDoneTaskClick,
            navToEditActivity, onDateClickListener, navController
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksListContent(
    tasksList: List<Task>,
    viewModel: TasksViewModel,
    onDoneTaskClick: (Task) -> Unit,
    navToEditActivity: (Task) -> Unit,
    onDateClickListener: (Long) -> Unit,
    navController: NavController?
) {

    if (langIndex == 1) {
        Locale.setDefault(Locale("ar"))
    }
    Scaffold(
        topBar = {
            TopAppBar(stringResource(id = R.string.app_title))
            Calendar(viewModel, onDateClickListener)
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TaskList(
                tasksList = tasksList,
                viewModel,
                onDoneTaskClick = onDoneTaskClick,
                navToEditActivity,
                navController
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(viewModel: TasksViewModel, onDateClickListener: (Long) -> Unit) {

    // get CalendarUiModel from CalendarDataSource, and the lastSelectedDate is Today.
    var calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp)
    ) {
        Header(data = calendarUiModel,
            onPrevClickListener = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
            },
            onNextClickListener = { endDate ->
                // refresh the CalendarUiModel with new data
                // by get data with new Start Date (which is the endDate+2 from the visibleDates)
                val finalStartDate = endDate.plusDays(2)
                calendarUiModel = dataSource.getData(
                    startDate = finalStartDate,
                    lastSelectedDate = calendarUiModel.selectedDate.date
                )
            }
        )
        Content(
            viewModel,
            data = calendarUiModel, onClickListener = { date ->
                // refresh the CalendarUiModel with new data
                // by changing only the `selectedDate` with the date selected by User
                calendarUiModel = calendarUiModel.copy(
                    selectedDate = date,
                    visibleDates = calendarUiModel.visibleDates.map {
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        )
                    }
                )
            },
            onDateClickListener = onDateClickListener
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Header(
    data: CalendarUiModel,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit,
) {

    Row {
        IconButton(modifier = Modifier.padding(end = 1.dp),
            onClick = {
                if (langIndex == 1) {
                    onNextClickListener(data.endDate.date)
                } else {
                    onPrevClickListener(data.startDate.date)
                }
            }) {
            Icon(
                imageVector =
                if (langIndex == 1) {
                    Icons.Filled.KeyboardArrowRight
                } else {
                    Icons.Filled.KeyboardArrowLeft
                },
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
        Text(
            // show "Today" if user selects today's date
            // else, show the full format of the date
            text = if (data.selectedDate.isToday) {
                stringResource(id = R.string.today)
            } else {
                data.selectedDate.date.format(
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                )
            },
            style = TextStyle(color = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier
                .padding(start = 2.dp)
                .weight(2f)
                .align(Alignment.CenterVertically)
        )
        IconButton(modifier = Modifier.padding(start = 1.dp),
            onClick = {
                if (langIndex == 1) {
                    onPrevClickListener(data.startDate.date)
                } else {
                    onNextClickListener(data.endDate.date)
                }
            }) {
            Icon(
                imageVector =
                if (langIndex == 1) {
                    Icons.Filled.KeyboardArrowLeft
                } else {
                    Icons.Filled.KeyboardArrowRight
                },
                contentDescription = "Next",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Content(
    viewModel: TasksViewModel,
    data: CalendarUiModel,
    onClickListener: (CalendarUiModel.Date) -> Unit,
    onDateClickListener: (Long) -> Unit
) {
    LazyRow(
        modifier = Modifier.padding(5.dp)
    ) {
        // pass the visibleDates to the UI
        items(items = data.visibleDates) { date ->
            ContentItem(viewModel, date = date, onClickListener, onDateClickListener)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentItem(
    viewModel: TasksViewModel,
    date: CalendarUiModel.Date,
    onClickListener: (CalendarUiModel.Date) -> Unit,
    onDateClickListener: (Long) -> Unit,
) {

    Card(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .clickable {
                onClickListener(date)
                dateTime =
                    date.date
                        .atStartOfDay(ZoneOffset.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                viewModel.getTasksByDay(dateTime)
                onDateClickListener(dateTime)
            },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSecondary
        ),
    ) {
        Column(
            modifier = Modifier
                .width(48.dp)
                .height(55.dp)
                .padding(6.dp)
        ) {
            Text(
                text = date.day,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = if (date.isSelected) {
                    LightPrimary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }
            )
            Text(
                text = date.date.dayOfMonth.toString(), // date "15", "16"
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if (date.isSelected) {
                    LightPrimary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "TasksList", showSystemUi = true)
@Composable
fun TasksListPreview() {
//    TasksList(tasksList = listOf())
}