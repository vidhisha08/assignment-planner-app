package com.example.studybuddy

import android.content.Context
import com.example.studybuddy.data.AppDatabase
import com.example.studybuddy.data.SessionManager
import com.example.studybuddy.data.repository.AssignmentRepository
import com.example.studybuddy.data.repository.AuthRepository


class AppContainer(context: Context) {


    private val database = AppDatabase.getInstance(context)

    val authRepository       = AuthRepository(database.userDao())
    val assignmentRepository = AssignmentRepository(database.assignmentDao())
    val sessionManager       = SessionManager(context)
}
