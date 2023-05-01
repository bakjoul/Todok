package com.bakjoul.todok.domain.task

import com.bakjoul.todok.data.dao.TaskDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteTaskUseCase @Inject constructor(private val taskDao: TaskDao) {
    suspend fun invoke(taskId: Long) {
        taskDao.delete(taskId)
    }
}
