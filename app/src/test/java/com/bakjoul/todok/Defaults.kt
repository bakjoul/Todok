package com.bakjoul.todok

import com.bakjoul.todok.domain.project.ProjectEntity
import com.bakjoul.todok.domain.task.TaskEntity
import com.bakjoul.todok.ui.add_tasks.AddTaskProjectItemViewState

fun getDefaultProjectEntity(projectId: Long) = ProjectEntity(
    id = projectId,
    name = "Project name: $projectId",
    color = projectId.toInt()
)

fun getDefaultTaskEntity(taskId: Long, projectId: Long) = TaskEntity(
    id = taskId,
    projectId = projectId,
    description = "Task description: $taskId"
)

fun getDefaultTaskEntities() = listOf(
    getDefaultTaskEntity(taskId = 0, projectId = 0),
    getDefaultTaskEntity(taskId = 1, projectId = 1),
    getDefaultTaskEntity(taskId = 2, projectId = 2)
)

fun getDefaultProjectEntities() = listOf(
    getDefaultProjectEntity(projectId = 0),
    getDefaultProjectEntity(projectId = 1),
    getDefaultProjectEntity(projectId = 2)
)

fun getDefaultProjectItemViewState(projectId: Long) = AddTaskProjectItemViewState(
    projectId = projectId,
    projectColor = getDefaultProjectEntity(projectId).color,
    projectName = getDefaultProjectEntity(projectId).name,
)
