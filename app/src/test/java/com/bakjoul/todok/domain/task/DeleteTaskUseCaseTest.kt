package com.bakjoul.todok.domain.task

import com.bakjoul.todok.data.dao.TaskDao
import com.bakjoul.todok.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeleteTaskUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskDao: TaskDao = mockk()

    private val deleteTaskUseCase = DeleteTaskUseCase(taskDao)

    @Before
    fun setUp() {
        coJustRun { taskDao.delete(any()) }
    }

    @Test
    fun verify() = testCoroutineRule.runTest {
        // Given
        val taskId = 123L

        // When
        deleteTaskUseCase.invoke(taskId)

        // Then
        coVerify(exactly = 1) { taskDao.delete(taskId) }
        confirmVerified(taskDao)
    }
}
