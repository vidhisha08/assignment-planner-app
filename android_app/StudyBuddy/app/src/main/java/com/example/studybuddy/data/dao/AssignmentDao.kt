package com.example.studybuddy.data.dao

import androidx.room.*
import com.example.studybuddy.data.entity.AssignmentEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface AssignmentDao {

    @Query("SELECT * FROM assignments WHERE userId = :userId ORDER BY dueDate ASC")
    fun getAllForUser(userId: Int): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE userId = :userId AND isCompleted = 0 ORDER BY dueDate ASC")
    fun getPendingForUser(userId: Int): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE userId = :userId AND isCompleted = 1 ORDER BY dueDate ASC")
    fun getCompletedForUser(userId: Int): Flow<List<AssignmentEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(assignment: AssignmentEntity)

    @Update
    suspend fun update(assignment: AssignmentEntity)

    @Delete
    suspend fun delete(assignment: AssignmentEntity)

    @Query("SELECT * FROM assignments WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): AssignmentEntity?
}
