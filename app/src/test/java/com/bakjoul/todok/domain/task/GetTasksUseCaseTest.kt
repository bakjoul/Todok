package com.bakjoul.todok.domain.task

import com.bakjoul.todok.data.dao.TaskDao
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTasksUseCaseTest {

    companion object {
        private val EXPECTED_FLOW: Flow<List<TaskEntity>> = flowOf()
    }

    private val taskDao: TaskDao = mockk()

    private val getTasksUseCase = GetTasksUseCase(taskDao)

    @Before
    fun setUp() {
        every { taskDao.getAllTasks() } returns EXPECTED_FLOW
    }

    @Test
    fun verify() {
        // When
        val result = getTasksUseCase.invoke()

        // Then
        assertEquals(result, EXPECTED_FLOW)
        verify(exactly = 1) { taskDao.getAllTasks() }
        confirmVerified(taskDao)
    }
}
