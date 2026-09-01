package com.example.studybuddy

import android.app.Application


class StudyBuddyApp : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
