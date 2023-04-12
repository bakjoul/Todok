package com.bakjoul.todok.data

import android.app.Application
import androidx.work.WorkManager
import com.bakjoul.todok.data.dao.ProjectDao
import com.bakjoul.todok.data.dao.TaskDao
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideWorkManager(application: Application): WorkManager = WorkManager.getInstance(application)

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideAppDatabase(
        application: Application,
        workManager: WorkManager,
        gson: Gson
    ): AppDatabase = AppDatabase.getInstance(application, workManager, gson)

    @Singleton
    @Provides
    fun provideProjectDao(appDatabase: AppDatabase): ProjectDao = appDatabase.getProjectDao()

    @Singleton
    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao = appDatabase.getTaskDao()
}