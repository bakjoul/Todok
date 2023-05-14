package com.bakjoul.todok.data

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.bakjoul.todok.domain.project.InsertProjectUseCase
import com.bakjoul.todok.getDefaultProjectEntitiesAsJson
import com.bakjoul.todok.getDefaultProjectEntity
import com.bakjoul.todok.utils.TestCoroutineRule
import com.google.gson.Gson
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InitializeDatabaseWorkerTest {

    companion object {
        private const val KEY_INPUT_DATA = "KEY_INPUT_DATA"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val context: Context = mockk()
    private val workerParams: WorkerParameters = mockk(relaxed = true)
    private val insertProjectUseCase: InsertProjectUseCase = mockk()
    private val gson: Gson = DataModule().provideGson()

    private val initializeDatabaseWorker = spyk(
        InitializeDatabaseWorker(
            context = context,
            workerParams = workerParams,
            insertProjectUseCase = insertProjectUseCase,
            gson = gson,
            coroutineDispatcherProvider = testCoroutineRule.getTestCoroutineDispatcherProvider()
        )
    )

    @Before
    fun setUp() {
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns getDefaultProjectEntitiesAsJson()
        coJustRun { insertProjectUseCase.invoke(any()) }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertThat(result).isEqualTo(ListenableWorker.Result.success())
        coVerify(exactly = 1) {
            initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA)
            insertProjectUseCase.invoke(getDefaultProjectEntity(0))
            insertProjectUseCase.invoke(getDefaultProjectEntity(1))
            insertProjectUseCase.invoke(getDefaultProjectEntity(2))
        }
        confirmVerified(insertProjectUseCase)
    }

    @Test
    fun `error case - no input data`() = testCoroutineRule.runTest {
        // Given
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns null

        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
        confirmVerified(insertProjectUseCase)
    }

    @Test
    fun `error case - wrong input data`() = testCoroutineRule.runTest {
        // Given
        every { initializeDatabaseWorker.inputData.getString(KEY_INPUT_DATA) } returns "wrong input data"

        // When
        val result = initializeDatabaseWorker.doWork()

        // Then
        assertThat(result).isEqualTo(ListenableWorker.Result.failure())
        confirmVerified(insertProjectUseCase)
    }
}
