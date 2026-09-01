# assignment-planner-app
StudyBuddy 📚

An assignment planner built with Kotlin and Jetpack Compose for Android.

App overview

StudyBuddy helps students organise and track their assignments in one place. Users can register an account, add assignments with a title, module code, due date and priority, mark them as complete, edit them, and delete them. All data is saved locally on the device using Room.

Screens

* Screen 1 -- AuthScreen.kt: Login and Register (tab toggle on one screen)
* Screen 2 --	ListScreen.kt:	Main assignment list with filter tabs
* Screen 3 --	AddEditScreen.kt:	Add a new assignment or edit an existing one

Password security

Passwords are never stored on the device in plain text. When a user registers:

A random 16-byte salt is generated using SecureRandom
The salt is combined with the password and run through SHA-256
Only the hash and salt are saved to the Room database

When logging in, the same process runs again and the two hashes are compared. If they match, login succeeds. The real password is never saved anywhere.

Colour palette

* Lavender - #C9B8F0	
* Mint - #5AAB88	
* Blush pink - #F4C0D1	
* Butter - #FEF9E4	
* White - #FFFFFF

Preview of the App

https://drive.google.com/file/d/1RW08L-5qB8fr0E-2Vvsn3DfP3xS4uIdk/view?usp=sharing

