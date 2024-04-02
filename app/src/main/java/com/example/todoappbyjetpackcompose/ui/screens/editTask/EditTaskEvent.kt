package com.example.todoappbyjetpackcompose.ui.screens.editTask

sealed interface EditTaskEvent {
    object NavigateBack : EditTaskEvent
}