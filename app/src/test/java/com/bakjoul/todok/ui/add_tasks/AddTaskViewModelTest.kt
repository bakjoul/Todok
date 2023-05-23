package com.bakjoul.todok.ui.add_tasks

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.bakjoul.todok.R
import com.bakjoul.todok.domain.project.GetProjectsUseCase
import com.bakjoul.todok.domain.task.InsertTaskUseCase
import com.bakjoul.todok.domain.task.TaskEntity
import com.bakjoul.todok.getDefaultProjectEntities
import com.bakjoul.todok.getDefaultProjectItemViewState
import com.bakjoul.todok.utils.TestCoroutineRule
import com.bakjoul.todok.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddTaskViewModelTest {

    companion object {
        private const val DEFAULT_TASK_DESCRIPTION = "DEFAULT_DESCRIPTION"
        private const val DEFAULT_PROJECT_ID = 1L
        private const val ERROR_TASK_DESCRIPTION = "Veuillez saisir une tâche"
        private const val ERROR_PROJECT = "Veuillez selectionner un projet"
        private const val ERROR_TASK_INSERTION = "Impossible d'ajouter la tâche…"
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val application: Application = mockk()
    private val insertTaskUseCase: InsertTaskUseCase = mockk()
    private val getProjectsUseCase: GetProjectsUseCase = mockk()

    private val addTaskViewModel = AddTaskViewModel(
        application = application,
        insertTaskUseCase = insertTaskUseCase,
        getProjectsUseCase = getProjectsUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        coEvery { insertTaskUseCase.invoke(any()) } coAnswers { true }
        every { getProjectsUseCase.invoke() } returns flowOf(getDefaultProjectEntities())

        every { application.getString(R.string.add_task_dialog_error_task_description) } returns ERROR_TASK_DESCRIPTION
        every { application.getString(R.string.add_task_dialog_error_project) } returns ERROR_PROJECT
        every { application.getString(R.string.add_task_insertion_error_toast) } returns ERROR_TASK_INSERTION
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                getExpectedAddTaskViewState()
            )
        }
    }

    @Test
    fun `nominal case - add button clicked with valid inputs`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onTaskDescriptionChanged(DEFAULT_TASK_DESCRIPTION)
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
        addTaskViewModel.onAddButtonClicked()

        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getExpectedAddTaskViewState())
            assertThat(addTaskViewModel.singleLiveEvent.value).isEqualTo(AddTaskEvent.Dismiss)
        }
    }

    @Test
    fun `error case - add button clicked with all input null`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onAddButtonClicked()

        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                getExpectedAddTaskViewState().copy(
                    taskDescriptionError = ERROR_TASK_DESCRIPTION,
                    projectError = ERROR_PROJECT,
                )
            )
        }
    }

    @Test
    fun `error case - add button clicked with task description null`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
        addTaskViewModel.onAddButtonClicked()

        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(

                getExpectedAddTaskViewState().copy(
                    taskDescriptionError = ERROR_TASK_DESCRIPTION,
                )
            )
        }
    }

    @Test
    fun `error case - add button clicked with task description blank`() =
        testCoroutineRule.runTest {
            // Given
            addTaskViewModel.onTaskDescriptionChanged(" ")
            addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)
            addTaskViewModel.onAddButtonClicked()

            // When
            addTaskViewModel.viewStateLiveData.observeForTesting(this) {

                // Then
                assertThat(it.value).isEqualTo(
                    getExpectedAddTaskViewState().copy(
                        taskDescriptionError = ERROR_TASK_DESCRIPTION,
                    )
                )
            }
        }

    @Test
    fun `error case - add button clicked with project id null`() = testCoroutineRule.runTest {
        // Given
        addTaskViewModel.onTaskDescriptionChanged(DEFAULT_TASK_DESCRIPTION)
        addTaskViewModel.onAddButtonClicked()

        // When
        addTaskViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(
                getExpectedAddTaskViewState().copy(
                    projectError = ERROR_PROJECT,
                )
            )
        }
    }

    @Test
    fun `error case - add button clicked with valid inputs but insertion error`() =
        testCoroutineRule.runTest {
            // Given
            coEvery { insertTaskUseCase.invoke(any()) } returns false
            addTaskViewModel.onTaskDescriptionChanged(DEFAULT_TASK_DESCRIPTION)
            addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)

            // When
            addTaskViewModel.onAddButtonClicked()
            runCurrent()

            // Then
            assertThat(addTaskViewModel.singleLiveEvent.value).isEqualTo(AddTaskEvent.Toast(R.string.add_task_insertion_error_toast))
            coVerify(exactly = 1) {
                insertTaskUseCase.invoke(
                    TaskEntity(
                        description = DEFAULT_TASK_DESCRIPTION,
                        projectId = DEFAULT_PROJECT_ID
                    )
                )
            }
            confirmVerified(insertTaskUseCase)
        }

    @Test
    fun `edge case - task description blank and project id null without add button clicked`() =
        testCoroutineRule.runTest {
            // Given
            addTaskViewModel.onTaskDescriptionChanged(" ")

            // When
            addTaskViewModel.viewStateLiveData.observeForTesting(this) {

                // Then
                assertThat(it.value).isEqualTo(getExpectedAddTaskViewState())
            }
        }

    @Test
    fun `edge case - task description null without add button clicked`() =
        testCoroutineRule.runTest {
            // Given
            addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)

            // When
            addTaskViewModel.viewStateLiveData.observeForTesting(this) {

                // Then
                assertThat(it.value).isEqualTo(getExpectedAddTaskViewState())
            }
        }

    @Test
    fun `edge case - task description blank without add button clicked`() =
        testCoroutineRule.runTest {
            // Given
            addTaskViewModel.onTaskDescriptionChanged(" ")
            addTaskViewModel.onProjectSelected(DEFAULT_PROJECT_ID)

            // When
            addTaskViewModel.viewStateLiveData.observeForTesting(this) {

                // Then
                assertThat(it.value).isEqualTo(getExpectedAddTaskViewState())
            }
        }

    // region OUT
    private fun getExpectedAddTaskViewState() = AddTaskViewState(
        listOf(
            getDefaultProjectItemViewState(0),
            getDefaultProjectItemViewState(1),
            getDefaultProjectItemViewState(2)
        ),
        null,
        null
    )
    // endregion OUT
}
