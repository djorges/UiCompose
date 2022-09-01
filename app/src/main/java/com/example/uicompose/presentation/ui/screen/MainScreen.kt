package com.example.uicompose.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uicompose.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val viewModel = hiltViewModel<MainViewModel>()

    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
             HomeScreen(navController, viewModel)
        }
    }
}