package com.example.uicompose.presentation.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.uicompose.presentation.viewmodel.MainViewModel

/**
 * TODO:
 * Crear ListItem
 * min 35:16  https://youtu.be/Z2fwVf-SJCs
 *
 * */
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    Text(
        text = "this is a text",
        modifier = Modifier
            .fillMaxWidth(),
        fontSize = 22.sp,
        textAlign = TextAlign.Center
    )
}