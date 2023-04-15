package com.bakjoul.todok.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.bakjoul.todok.data.CoroutineDispatcherProvider
import com.bakjoul.todok.domain.project.GetProjectsUseCase
import com.bakjoul.todok.domain.project.ProjectEntity
import com.bakjoul.todok.domain.task.DeleteTaskUseCase
import com.bakjoul.todok.domain.task.GetTasksUseCase
import com.bakjoul.todok.domain.task.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val taskSortingMutableStateFlow = MutableStateFlow(TaskSortingType.TASK_CHRONOLOGICAL)

    val viewStateLiveData: LiveData<List<TaskViewStateItem>> =
        liveData(coroutineDispatcherProvider.io) {
            combine(
                getTasksUseCase.invoke(),
                getProjectsUseCase.invoke(),
                taskSortingMutableStateFlow
            ) { tasks, projects, taskSorting ->
                emit(
                    when (taskSorting) {
                        TaskSortingType.TASK_CHRONOLOGICAL -> tasks.asSequence()
                            .flatMap { task ->
                                val project = projects.first() { it.id == task.projectId }
                                sequenceOf(mapItem(task, project))
                            }
                            .sortedBy { it.taskId }
                            .toList()
                        TaskSortingType.TASK_REVERSE_CHRONOLOGICAL -> tasks.asSequence()
                            .flatMap { task ->
                                val project = projects.first() { it.id == task.projectId }
                                sequenceOf(mapItem(task, project))
                            }
                            .sortedByDescending { it.taskId }
                            .toList()
                        TaskSortingType.PROJECT_ALPHABETICAL -> tasks.asSequence()
                            .flatMap { task ->
                                val project = projects.first() { it.id == task.projectId }
                                sequenceOf(mapItem(task, project))
                            }
                            .sortedBy { it.project }
                            .toList()
                        TaskSortingType.PROJECT_REVERSE_ALPHABETICAL -> tasks.asSequence()
                            .flatMap { task ->
                                val project = projects.first() { it.id == task.projectId }
                                sequenceOf(mapItem(task, project))
                            }
                            .sortedByDescending { it.project }
                            .toList()
                    }
                )
            }.collect()

        }

    private fun mapItem(task: TaskEntity, project: ProjectEntity): TaskViewStateItem {
        return TaskViewStateItem(
            taskId = task.id,
            projectColor = project.color,
            description = task.description,
            project = project.name
        )
    }

}
