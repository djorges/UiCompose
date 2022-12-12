package com.example.uicompose.util.compose.foundation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay

/**
 * Example 1 :SideEffect
 *
 * Note: share Compose state with objects not managed
 * by compose, use the SideEffect composable, as it's invoked
 * on every successful recomposition
 **/
@Composable
fun SideEffectExample(){
    var clicks by remember{ mutableStateOf(0)}
    SideEffect {
        Log.i("UICOMPOSE_APP","Recomposed Successfully $clicks times")
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
 *
 *
 * To call suspend functions safely from inside a composable, use the LaunchedEffect composable.
 * When LaunchedEffect enters the Composition, it launches a coroutine with
 * the block of code passed as a parameter. The coroutine will be cancelled if
 * LaunchedEffect leaves the composition. If LaunchedEffect is recomposed with
 * different keys (see the Restarting Effects section below), the existing coroutine will be
 * cancelled and the new suspend function will be launched in a new coroutine
 * */
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
 * Example 3:DisposableEffect(Unit)
 *
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
/**
 * Example 5: produceState
 *
 * Note: produceState makes use of other effects!
 * It holds a result variable using remember { mutableStateOf(initialValue) },
 * and triggers the producer block in a LaunchedEffect.
 * Whenever value is updated in the producer block,
 * the result state is updated to the new value.
 * */
@Composable
fun ProduceStateExample(
    url:String = "some url"
){
    var counter = produceState(
        initialValue = 0,
        url
    ){
        delay(10000L)
        value = 5
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = counter.value.toString(),
            fontSize = 20.sp
        )
    }
}

