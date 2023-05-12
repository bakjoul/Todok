package com.bakjoul.todok.domain.task

import android.database.sqlite.SQLiteException
import com.bakjoul.todok.data.dao.TaskDao
import com.bakjoul.todok.getDefaultTaskEntity
import com.bakjoul.todok.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertTaskUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val taskDao: TaskDao = mockk()

    private val insertTaskUseCase = InsertTaskUseCase(taskDao)

    @Before
    fun setUp() {
        coJustRun { taskDao.insert(any()) }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = insertTaskUseCase.invoke(getDefaultTaskEntity(1, 0))

        // Then
        assertTrue(result)
        coVerify(exactly = 1) { taskDao.insert(getDefaultTaskEntity(1, 0)) }
        confirmVerified(taskDao)
    }

    @Test
    fun `error case - SQLiteException is thrown`() = testCoroutineRule.runTest {
        // Given
        coEvery { taskDao.insert(any()) } throws SQLiteException()

        // When
        val result = insertTaskUseCase.invoke(getDefaultTaskEntity(1, 0))

        // Then
        assertFalse(result)
        coVerify(exactly = 1) { taskDao.insert(getDefaultTaskEntity(1, 0)) }
        confirmVerified(taskDao)
    }
}
