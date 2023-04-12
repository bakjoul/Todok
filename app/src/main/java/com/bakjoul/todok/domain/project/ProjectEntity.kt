package com.bakjoul.todok.domain.project

import androidx.annotation.ColorInt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "project")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    @ColorInt
    val color: Int
)