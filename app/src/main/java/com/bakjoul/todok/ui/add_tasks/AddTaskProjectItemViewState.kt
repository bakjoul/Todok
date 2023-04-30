package com.bakjoul.todok.ui.add_tasks

import androidx.annotation.ColorInt

data class AddTaskProjectItemViewState(
    val projectId: Long,
    @get:ColorInt @param:ColorInt
    val projectColor: Int,
    val projectName: String,
)
