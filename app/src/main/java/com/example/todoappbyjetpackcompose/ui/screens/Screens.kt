package com.example.todoappbyjetpackcompose.ui.screens

sealed class Screens(val screen: String) {
    object TasksList : Screens("tasks list")
    object Settings : Screens("settings")
    object EditTask : Screens("edit task")
}