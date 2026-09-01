package com.example.studybuddy.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// Index(unique = true) on email means no two users can share the same email.
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String,
    val passwordHash: String,   // real password is not stored, only the hash
    val salt: String            // a random string that is mixed with the password before hashing
)
