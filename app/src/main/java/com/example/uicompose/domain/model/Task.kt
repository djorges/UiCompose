package com.example.uicompose.domain.model

data class Task(
    val id:Int,
    val message:String,
    var checked: Boolean = false
)