package com.bakjoul.todok.domain.project

import com.bakjoul.todok.data.dao.ProjectDao
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProjectsUseCaseTest {

    companion object {
        private val EXPECTED_FLOW: Flow<List<ProjectEntity>> = flowOf()
    }

    private val projectDao: ProjectDao = mockk()

    private val getProjectsUseCase = GetProjectsUseCase(projectDao)

    @Before
    fun setUp() {
        every { projectDao.getAllProjects() } returns EXPECTED_FLOW
    }

    @Test
    fun verify() {
        // When
        val result = getProjectsUseCase.invoke()

        // Then
        assertEquals(result, EXPECTED_FLOW)
        verify(exactly = 1) { projectDao.getAllProjects() }
        confirmVerified(projectDao)
    }
}
