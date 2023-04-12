package com.bakjoul.todok.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bakjoul.todok.domain.task.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM task WHERE id=:taskId")
    suspend fun delete(taskId: Long)

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<TaskEntity>>
}
