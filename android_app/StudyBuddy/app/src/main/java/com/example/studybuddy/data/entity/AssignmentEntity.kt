package com.example.studybuddy.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class AssignmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val moduleCode: String,
    val notes: String = "",
    val dueDate: Long,
    val priority: String = "Medium",
    val isCompleted: Boolean = false,
    val userId: Int
)
