package com.example.studybuddy.data

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("studybuddy_prefs", Context.MODE_PRIVATE)

    fun saveUser(userId: Int, email: String) {
        prefs.edit()
            .putInt("user_id", userId)
            .putString("user_email", email)
            .apply()
    }

    fun getUserId(): Int = prefs.getInt("user_id", -1)

    fun getUserEmail(): String? = prefs.getString("user_email", null)

    fun isLoggedIn(): Boolean = getUserId() != -1

    fun logout() {
        prefs.edit().clear().apply()
    }
}
