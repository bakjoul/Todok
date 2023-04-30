package com.bakjoul.todok.ui.add_tasks

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.bakjoul.todok.R
import com.bakjoul.todok.domain.CoroutineDispatcherProvider
import com.bakjoul.todok.domain.project.GetProjectsUseCase
import com.bakjoul.todok.domain.task.InsertTaskUseCase
import com.bakjoul.todok.domain.task.TaskEntity
import com.bakjoul.todok.ui.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddTaskViewModel @Inject constructor(
    private val application: Application,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : ViewModel() {

    private val taskDescriptionMutableStateFlow = MutableStateFlow<String?>(null)
    private val projectIdMutableStateFlow = MutableStateFlow<Long?>(null)
    private val hasAddButtonBeenClicked = MutableStateFlow(false)

    private var taskDescription: String? = null
    private var projectId: Long? = null

    val singleLiveEvent = SingleLiveEvent<AddTaskEvent>()

    val viewStateLiveData: LiveData<AddTaskViewState> = liveData {
        combine(
            getProjectsUseCase.invoke(),
            taskDescriptionMutableStateFlow,
            projectIdMutableStateFlow,
            hasAddButtonBeenClicked
        ) { projects, taskDescription, projectId, addButtonClicked ->
            emit(
                AddTaskViewState(
                    items = projects.map { project ->
                        AddTaskProjectItemViewState(
                            projectId = project.id,
                            projectColor = project.color,
                            projectName = project.name
                        )
                    },
                    taskDescriptionError = if (taskDescription.isNullOrEmpty() && addButtonClicked) {
                        application.getString(R.string.add_task_dialog_error_task_description)
                    } else {
                        null
                    },
                    projectError = if (projectId == null && addButtonClicked) {
                        application.getString(R.string.add_task_dialog_error_project)
                    } else {
                        null
                    }
                )
            )
        }.collect()
    }

    fun onTaskDescriptionChanged(taskDescription: String?) {
        taskDescriptionMutableStateFlow.value = taskDescription
        this.taskDescription = taskDescription
    }

    fun onProjectSelected(projectId: Long) {
        projectIdMutableStateFlow.value = projectId
        this.projectId = projectId
    }

    fun onAddButtonClicked() {
        hasAddButtonBeenClicked.value = true

        val capturedTaskDescription = taskDescription
        val capturedProjectId = projectId

        if (!capturedTaskDescription.isNullOrEmpty() && capturedProjectId != null) {
            viewModelScope.launch(coroutineDispatcherProvider.io) {
                val success = insertTaskUseCase.invoke(
                    TaskEntity(
                        description = capturedTaskDescription,
                        projectId = capturedProjectId
                    )
                )

                withContext(coroutineDispatcherProvider.main) {
                    if (success) {
                        singleLiveEvent.value = AddTaskEvent.Dismiss
                    }
                }
            }
        }
    }
}
