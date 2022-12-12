package com.example.uicompose.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id:Int? = null,
    @ColumnInfo(name="name")
    val name:String,
    @ColumnInfo(name = "last_name")
    val lastName:String,
    val age:Int
)