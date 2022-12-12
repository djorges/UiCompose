package com.example.uicompose.data.repository

import com.example.uicompose.data.entity.User
import com.example.uicompose.data.db.UsersDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database:UsersDB
){
    fun getUsers():Flow<List<User>>{
        return database.userDao.getUsers()
    }
    suspend fun getUserById(id:Int):User?{
        return database.userDao.getById(id)
    }
    suspend fun insertUser(user: User){
        database.userDao.insertUser(user)
    }
    suspend fun deleteUser(user:User){
        database.userDao.deleteUser(user)
    }
}