package com.bakjoul.todok.ui.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.bakjoul.todok.domain.project.GetProjectsUseCase
import com.bakjoul.todok.domain.task.DeleteTaskUseCase
import com.bakjoul.todok.domain.task.GetTasksUseCase
import com.bakjoul.todok.getDefaultProjectEntities
import com.bakjoul.todok.getDefaultTaskEntities
import com.bakjoul.todok.getDefaultTaskItemViewState
import com.bakjoul.todok.utils.TestCoroutineRule
import com.bakjoul.todok.utils.observeForTesting
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getTasksUseCase: GetTasksUseCase = mockk()
    private val getProjectsUseCase: GetProjectsUseCase = mockk()
    private val deleteTaskUseCase: DeleteTaskUseCase = mockk()

    private val tasksViewModel = TasksViewModel(
        getTasksUseCase = getTasksUseCase,
        getProjectsUseCase = getProjectsUseCase,
        deleteTaskUseCase = deleteTaskUseCase,
        coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        every { getTasksUseCase.invoke() } returns flowOf(getDefaultTaskEntities())
        every { getProjectsUseCase.invoke() } returns flowOf(getDefaultProjectEntities())
        coJustRun { deleteTaskUseCase.invoke(any()) }
    }

    @Test
    fun `initial case - sorted chronologically`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultTaskItemViewStates())
        }
    }

    @Test
    fun `nominal case - sorted chronologically reversed`() = testCoroutineRule.runTest {
        // Given
        tasksViewModel.onSortingTypeChanged(TaskSortingType.TASK_REVERSE_CHRONOLOGICAL)

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultTaskItemViewStates().asReversed())
        }
    }

    @Test
    fun `nominal case - sorted alphabetically by project`() = testCoroutineRule.runTest {
        // Given
        tasksViewModel.onSortingTypeChanged(TaskSortingType.PROJECT_ALPHABETICAL)

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultTaskItemViewStates())
        }
    }

    @Test
    fun `nominal case - sorted alphabetically reversed by project`() = testCoroutineRule.runTest {
        // Given
        tasksViewModel.onSortingTypeChanged(TaskSortingType.PROJECT_REVERSE_ALPHABETICAL)

        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(getDefaultTaskItemViewStates().asReversed())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `edge case - delete task`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.viewStateLiveData.observeForTesting(this) {
            (it.value?.first() as TaskItemViewState).onDeleteEvent.invoke()
            runCurrent()

            // Then
            coVerify(exactly = 1) { deleteTaskUseCase.invoke(0) }
            confirmVerified(deleteTaskUseCase)
        }
    }

    @Test
    fun `verify onAddButtonClicked`() = testCoroutineRule.runTest {
        // When
        tasksViewModel.onAddButtonClicked()

        // Then
        assertThat(tasksViewModel.singleLiveEvent.value).isEqualTo(TasksEvent.DisplayAddTaskDialog)
    }

    // region OUT
    private fun getDefaultTaskItemViewStates() = listOf(
        getDefaultTaskItemViewState(0, 0),
        getDefaultTaskItemViewState(1, 1),
        getDefaultTaskItemViewState(2, 2)
    )
    // endregion OUT
}
