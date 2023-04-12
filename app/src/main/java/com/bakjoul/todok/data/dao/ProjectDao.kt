package com.bakjoul.todok.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.bakjoul.todok.domain.project.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    suspend fun insert(projectEntity: ProjectEntity)

    @Query("SELECT * FROM project")
    fun getAllProjects(): Flow<List<ProjectEntity>>
}
