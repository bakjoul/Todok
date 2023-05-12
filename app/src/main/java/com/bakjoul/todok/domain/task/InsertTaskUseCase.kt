package com.bakjoul.todok.domain.task

import android.database.sqlite.SQLiteException
import com.bakjoul.todok.data.dao.TaskDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertTaskUseCase @Inject constructor(private val taskDao: TaskDao) {
    suspend fun invoke(taskEntity: TaskEntity): Boolean = try {
        taskDao.insert(taskEntity)
        true
    } catch (e: SQLiteException) {
        e.printStackTrace()
        false
    }
}
