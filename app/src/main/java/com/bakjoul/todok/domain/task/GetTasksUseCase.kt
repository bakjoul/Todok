package com.bakjoul.todok.domain.task

import com.bakjoul.todok.data.dao.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetTasksUseCase @Inject constructor(private val taskDao: TaskDao) {
    fun invoke(): Flow<List<TaskEntity>> = taskDao.getAllTasks()
}
