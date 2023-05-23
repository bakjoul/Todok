package com.bakjoul.todok.ui.tasks

enum class TaskSortingType(val sort: (List<TaskItemViewState>) -> List<TaskItemViewState>) {
    TASK_CHRONOLOGICAL(
        sort = { items ->
            items.sortedBy { it.taskId }
        }
    ),
    TASK_REVERSE_CHRONOLOGICAL(
        sort = { items ->
            items.sortedByDescending { it.taskId }
        }
    ),
    PROJECT_ALPHABETICAL(
        sort = { items ->
            items.sortedBy { it.taskId }
        }
    ),
    PROJECT_REVERSE_ALPHABETICAL(
        sort = { items ->
            items.sortedByDescending { it.taskId }
        }
    )
}
