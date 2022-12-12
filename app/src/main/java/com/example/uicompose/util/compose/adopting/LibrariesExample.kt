package com.example.uicompose.util.compose.adopting

/**
 * Example 1: Activity
 *
 * */
/*
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
*/
/**
 *
 *
 * */