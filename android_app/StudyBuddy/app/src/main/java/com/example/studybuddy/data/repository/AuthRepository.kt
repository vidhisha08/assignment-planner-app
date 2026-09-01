package com.example.studybuddy.data.repository

import com.example.studybuddy.data.PasswordHasher
import com.example.studybuddy.data.dao.UserDao
import com.example.studybuddy.data.entity.UserEntity

class AuthRepository(private val dao: UserDao) {

    suspend fun register(email: String, password: String): Result<Int> {
        if (dao.emailExists(email) > 0) {
            return Result.failure(Exception("An account with this email already exists."))
        }
        val salt = PasswordHasher.generateSalt()
        val hash = PasswordHasher.hash(password, salt)
        val newUser = UserEntity(email = email, passwordHash = hash, salt = salt)
        val id = dao.insert(newUser)
        return if (id > 0) Result.success(id.toInt())
        else Result.failure(Exception("Registration failed. Please try again."))
    }

    suspend fun login(email: String, password: String): Result<UserEntity> {
        val user = dao.getByEmail(email)
            ?: return Result.failure(Exception("No account found for this email."))
        return if (PasswordHasher.verify(password, user.passwordHash, user.salt))
            Result.success(user)
        else
            Result.failure(Exception("Incorrect password. Please try again."))
    }
}
