package com.bakjoul.todok.domain.project

import com.bakjoul.todok.data.dao.ProjectDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertProjectUseCase @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun invoke(projectEntity: ProjectEntity) {
        projectDao.insert(projectEntity)
    }
}
