package com.bakjoul.todok.domain.project

import com.bakjoul.todok.data.dao.ProjectDao
import com.bakjoul.todok.getDefaultProjectEntity
import com.bakjoul.todok.utils.TestCoroutineRule
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InsertProjectUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val projectDao: ProjectDao = mockk()

    private val insertProjectUseCase = InsertProjectUseCase(projectDao)

    @Before
    fun setUp() {
        coJustRun { projectDao.insert(any()) }
    }

    @Test
    fun verify() = testCoroutineRule.runTest {
        // When
        insertProjectUseCase.invoke(getDefaultProjectEntity(0))

        // Then
        coVerify(exactly = 1) { projectDao.insert(getDefaultProjectEntity(0)) }
        confirmVerified(projectDao)
    }
}
