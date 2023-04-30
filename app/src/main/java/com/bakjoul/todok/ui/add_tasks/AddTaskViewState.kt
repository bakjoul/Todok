package com.bakjoul.todok.ui.add_tasks

data class AddTaskViewState(
    val items: List<AddTaskProjectItemViewState>,
    val taskDescriptionError: String?,
    val projectError: String?
)
