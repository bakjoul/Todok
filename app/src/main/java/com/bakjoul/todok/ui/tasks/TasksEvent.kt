package com.bakjoul.todok.ui.tasks

sealed class TasksEvent {
    object DisplayAddTaskDialog : TasksEvent()
}
