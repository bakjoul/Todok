package com.bakjoul.todok.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bakjoul.todok.data.utils.fromJson
import com.bakjoul.todok.domain.CoroutineDispatcherProvider
import com.bakjoul.todok.domain.project.InsertProjectUseCase
import com.bakjoul.todok.domain.project.ProjectEntity
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

@HiltWorker
class InitializeDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val insertProjectUseCase: InsertProjectUseCase,
    private val gson: Gson,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_INPUT_DATA = "KEY_INPUT_DATA"
    }

    override suspend fun doWork(): Result = withContext(coroutineDispatcherProvider.io) {
        val entitiesAsJson = inputData.getString(KEY_INPUT_DATA)

        if (entitiesAsJson != null) {
            val projectEntities = gson.fromJson<List<ProjectEntity>>(json = entitiesAsJson)

            if (projectEntities != null) {
                projectEntities.forEach { projectEntity ->
                    insertProjectUseCase.invoke(projectEntity)
                }
                Result.success()
            } else {
                Log.e(javaClass.simpleName, "Can't parse projects: $entitiesAsJson")
                Result.failure()
            }
        } else {
            Log.e(
                javaClass.simpleName,
                "Failed to get data with key $KEY_INPUT_DATA from data: $inputData"
            )
            Result.failure()
        }
    }
}
