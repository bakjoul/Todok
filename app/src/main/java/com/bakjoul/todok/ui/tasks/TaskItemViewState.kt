package com.bakjoul.todok.ui.tasks

import androidx.annotation.ColorInt

data class TaskItemViewState(
    val taskId: Long,
    @get:ColorInt @param:ColorInt
    val projectColor: Int,
    val description: String,
    val project: String
)
