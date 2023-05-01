package com.bakjoul.todok.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bakjoul.todok.domain.CoroutineDispatcherProvider
import com.bakjoul.todok.domain.project.GetProjectsUseCase
import com.bakjoul.todok.domain.project.ProjectEntity
import com.bakjoul.todok.domain.task.DeleteTaskUseCase
import com.bakjoul.todok.domain.task.GetTasksUseCase
import com.bakjoul.todok.domain.task.TaskEntity
import com.bakjoul.todok.ui.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val taskSortingMutableStateFlow = MutableStateFlow(TaskSortingType.TASK_CHRONOLOGICAL)

    val viewStateLiveData: LiveData<List<TaskItemViewState>> =
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

    val singleLiveEvent = SingleLiveEvent<TasksEvent>()

    private fun mapItem(task: TaskEntity, project: ProjectEntity): TaskItemViewState {
        return TaskItemViewState(
            taskId = task.id,
            projectColor = project.color,
            description = task.description,
            project = project.name,
            onDeleteEvent = {
                viewModelScope.launch(coroutineDispatcherProvider.io) {
                    deleteTaskUseCase.invoke(task.id)
                }
            }
        )
    }

    fun onSortingTypeChanged(taskSortingType: TaskSortingType) {
        taskSortingMutableStateFlow.value = taskSortingType
    }

    fun onAddButtonClicked() {
        singleLiveEvent.value = TasksEvent.DisplayAddTaskDialog
    }
}
