package com.example.studybuddy.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.studybuddy.data.dao.AssignmentDao
import com.example.studybuddy.data.dao.UserDao
import com.example.studybuddy.data.entity.AssignmentEntity
import com.example.studybuddy.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, AssignmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun assignmentDao(): AssignmentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studybuddy_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
