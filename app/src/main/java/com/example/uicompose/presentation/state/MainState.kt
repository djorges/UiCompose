package com.example.uicompose.presentation.state

import com.example.uicompose.domain.model.Task

data class MainState(
    val isLoading: Boolean = false,
    val coins:List<Task> = emptyList(),
    val error:String = ""
)