package com.example.uicompose.presentation.ui.screen.adopting

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * Example 1: Activity
 *
 * */
@Preview
@Composable
fun GetContentImageExample(){
    var imageUri by rememberSaveable{ mutableStateOf<Uri?>(null)}
    val launcher = rememberLauncherForActivityResult(GetContent()){
        imageUri = it
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Load Image")
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            painter = rememberAsyncImagePainter(imageUri),
            contentDescription = "My Image"
        )
    }

}

/**
 *
 *
 * */

