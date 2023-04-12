package com.bakjoul.todok.ui.tasks

import androidx.annotation.ColorInt

data class TaskViewStateItem(
    val taskId: Long,
    @ColorInt
    val projectColor: Int,
    val description: String,
    val project: String
)
