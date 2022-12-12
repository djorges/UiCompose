package com.example.uicompose.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.uicompose.data.entity.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class UsersDB : RoomDatabase(){
    abstract val userDao:UserDao
}