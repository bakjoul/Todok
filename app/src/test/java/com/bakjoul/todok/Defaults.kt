package com.bakjoul.todok

import com.bakjoul.todok.domain.project.ProjectEntity
import com.bakjoul.todok.domain.task.TaskEntity

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
