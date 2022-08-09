package com.example.uicompose.presentation.ui.screen.foundation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * Example 1 :SideEffect
 *
 **/
@Composable
fun SideEffectExample(){
    var clicks by remember{ mutableStateOf(0)}
    SideEffect {
        Log.i("UICOMPOSE_APP","Recomposed Successfully")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 16.dp
            )
    ) {
        Button(
            onClick = { clicks++ }
        ){
            Text(text = clicks.toString() )
        }
    }
}

/**
 * Example 2 : LaunchedEffect
 * */
//listOf<Movie>().toMutableStateList()
@Preview(
    showBackground = true,
    name = "sdassd",
    widthDp = 300,
    heightDp = 600
)
@Composable
fun LaunchedEffectExample() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    var counter by remember { mutableStateOf(0) }
    Scaffold(scaffoldState = scaffoldState) {
        if ( counter%5 == 0 && counter > 0){
            LaunchedEffect(scaffoldState.snackbarHostState){
                scaffoldState.snackbarHostState.showSnackbar("Hello")
            }
        }
        Button(onClick = { counter++}) {
            Text(text = "Click me: $counter")
        }
    }
}

/**
 * Example 3:DisposableEffect
 *
 * */
@Composable
fun DisposableEffectExample(){
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        // Add the observer to the lifecycle
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                Log.i("UICOMPOSE_APP","Screen Started")
            } else if (event == Lifecycle.Event.ON_STOP) {
                Log.i("UICOMPOSE_APP","Screen Stopped")
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        Log.i("UICOMPOSE_APP","Observer Added")

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            Log.i("UICOMPOSE_APP","Observer Removed")
        }
    }
}