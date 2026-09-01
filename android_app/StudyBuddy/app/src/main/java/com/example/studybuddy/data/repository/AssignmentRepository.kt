package com.example.studybuddy.data.repository

import com.example.studybuddy.data.dao.AssignmentDao
import com.example.studybuddy.data.entity.AssignmentEntity
import kotlinx.coroutines.flow.Flow

class AssignmentRepository(private val dao: AssignmentDao) {

    fun getAllForUser(userId: Int): Flow<List<AssignmentEntity>> =
        dao.getAllForUser(userId)

    fun getPendingForUser(userId: Int): Flow<List<AssignmentEntity>> =
        dao.getPendingForUser(userId)

    fun getCompletedForUser(userId: Int): Flow<List<AssignmentEntity>> =
        dao.getCompletedForUser(userId)

    suspend fun add(assignment: AssignmentEntity) = dao.insert(assignment)

    suspend fun update(assignment: AssignmentEntity) = dao.update(assignment)

    suspend fun delete(assignment: AssignmentEntity) = dao.delete(assignment)

    suspend fun getById(id: Int): AssignmentEntity? = dao.getById(id)
}
