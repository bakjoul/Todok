package com.bakjoul.todok.ui.add_tasks

sealed class AddTaskEvent {
    object Dismiss : AddTaskEvent()
}
