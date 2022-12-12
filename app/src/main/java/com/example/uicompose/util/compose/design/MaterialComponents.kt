package com.example.uicompose.util.compose.design

/***
 * MaterialUI3
 * Docs: https://m3.material.io/
 * Guide Compose Android: https://developer.android.com/jetpack/compose/layouts/material
 */

/**
 * Example: Scaffold + Floating Button + SnackBar + Drawer
 * */
/*
presentation/ui/MainActivity
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UiComposeTheme {
                val scaffoldState = rememberScaffoldState() // this contains the `SnackbarHostState`

                //
                var showSnackBar by remember { mutableStateOf(false)}
                if (showSnackBar) {
                    LaunchedEffect(scaffoldState.snackbarHostState) {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Error message",
                            actionLabel = "Retry message",
                            duration = SnackbarDuration.Short
                        )
                        when (result) {
                            SnackbarResult.Dismissed -> {
                                showSnackBar = false
                            }
                            SnackbarResult.ActionPerformed -> {
                                showSnackBar = false
                                // perform action here
                            }
                        }
                    }
                }
                Scaffold(
                    scaffoldState = scaffoldState,
                    //Top AppBar
                    topBar = { CustomTopAppBar() },
                    //FAB Button
                    floatingActionButton = { CustomFab() },
                    floatingActionButtonPosition = FabPosition.End,
                    //Drawer
                    drawerContent = { CustomDrawerContent() }
                ){
                    //MainScreen()

                    Button(
                        onClick = { showSnackBar = true }
                    ) {
                        Text(text = "Show SnackBar")
                    }
                }
            }
        }
    }
}
@Composable
fun CustomDrawerContent(){
    Text("Drawer title", modifier = Modifier.padding(16.dp))
    Divider()
}

@Preview(
    showBackground = true
)
@Composable
fun CustomFab(){
    FloatingActionButton(
        onClick = {  }
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Add"
        )
    }
}

@Composable
fun CustomTopAppBar(){
    TopAppBar(
        elevation = 1.dp
    ){
        Text(
            text = "UiCompose App"
        )
    }
}

*/
 /**
 *
 * https://developer.android.com/jetpack/compose/layouts/material#bottom-sheets
 * */