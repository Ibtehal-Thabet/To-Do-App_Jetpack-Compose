package com.example.todoappbyjetpackcompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.todoappbyjetpackcompose.ui.screens.Screens

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.List,
    val route: String = ""
) {

    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Todo List",
                icon = Icons.Filled.List,
                route = Screens.TasksList.screen
            ),
            BottomNavigationItem(
                label = "Settings",
                icon = Icons.Filled.Settings,
                route = Screens.Settings.screen
            )
        )
    }
}