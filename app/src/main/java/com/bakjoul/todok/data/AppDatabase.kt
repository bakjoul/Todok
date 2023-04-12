package com.bakjoul.todok.data

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.bakjoul.todok.R
import com.bakjoul.todok.data.dao.ProjectDao
import com.bakjoul.todok.data.dao.TaskDao
import com.bakjoul.todok.domain.project.ProjectEntity
import com.bakjoul.todok.domain.task.TaskEntity
import com.google.gson.Gson

@Database(
    entities = [ProjectEntity::class, TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProjectDao(): ProjectDao
    abstract fun getTaskDao(): TaskDao

    companion object {
        private const val DATABASE_NAME = "Todok_database"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(
            application: Application,
            workManager: WorkManager,
            gson: Gson
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val builder = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )

                builder.addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        val entitiesAsJson = gson.toJson(
                            listOf(
                                ProjectEntity(
                                    name = application.getString(R.string.project_tartampion),
                                    color = ResourcesCompat.getColor(
                                        application.resources,
                                        R.color.project_color_tartampion,
                                        null
                                    )
                                ),
                                ProjectEntity(
                                    name = application.getString(R.string.project_lucidia),
                                    color = ResourcesCompat.getColor(
                                        application.resources,
                                        R.color.project_color_lucidia,
                                        null
                                    )
                                ),
                                ProjectEntity(
                                    name = application.getString(R.string.project_circus),
                                    color = ResourcesCompat.getColor(
                                        application.resources,
                                        R.color.project_color_circus,
                                        null
                                    )
                                )
                            )
                        )

                        workManager.enqueue(
                            OneTimeWorkRequestBuilder<InitializeDatabaseWorker>()
                                .setInputData(workDataOf(InitializeDatabaseWorker.KEY_INPUT_DATA to entitiesAsJson))
                                .build()
                        )
                    }
                })

                val instance = builder.build()
                INSTANCE = instance
                instance
            }
        }
    }
}