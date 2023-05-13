package com.bakjoul.todok.ui.tasks

import androidx.annotation.ColorInt
import com.bakjoul.todok.ui.utils.EquatableCallback

data class TaskItemViewState(
    val taskId: Long,
    @get:ColorInt @param:ColorInt
    val projectColor: Int,
    val description: String,
    val project: String,
    val onDeleteEvent: EquatableCallback
)
