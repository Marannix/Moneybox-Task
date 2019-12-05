package com.example.minimoneybox.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.minimoneybox.data.user.User
import com.example.minimoneybox.data.UsersDao

@Database(
    entities = [User::class],
    version = 1
)
abstract class ApplicationDatabase : RoomDatabase() {
    abstract fun usersDao(): UsersDao
}