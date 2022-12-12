package com.example.uicompose.data.db

import androidx.room.*
import com.example.uicompose.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("Select * FROM users")
    fun getUsers(): Flow<List<User>>

    @Query("Select * FROM users WHERE id = :id")
    suspend fun getById(id:Int): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user:User)

    @Delete
    suspend fun deleteUser(user: User)
}