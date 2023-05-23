package com.bakjoul.todok.ui.add_tasks

import androidx.annotation.StringRes

sealed class AddTaskEvent {
    object Dismiss : AddTaskEvent()
    data class Toast(
        @StringRes
        val stringRes: Int
    ) : AddTaskEvent()
}
