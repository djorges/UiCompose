package com.example.uicompose.presentation.ui.state

import com.example.uicompose.data.entity.User

data class HomeState(
    val users: List<User>? = emptyList(),
    val error: String? = null,
    val isLoading:Boolean? = false
)
