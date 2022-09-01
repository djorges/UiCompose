package com.example.uicompose.data.util.compose.adopting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

/**
 * Example 1
 * */
@Composable
fun UserScreen(){
    val navController = rememberNavController()
    NavHost(navController = navController,startDestination = "profile") {
        composable("profile") { Profile(navController) }
        composable("friendsList") { FriendsList(/*...*/) }
    }
}

@Composable
fun FriendsList() {

}

@Composable
fun Profile(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("friendsList")}
        ) {
            Text(text = "Navigate next")
        }
    }
}

/**
 * Example 2 args
 *
 * */
@Composable
fun ProductScreen(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "list")
    {
        composable("list"){
            ProductList(navController)
        }
        composable(
            "details/{productId}",
            arguments = listOf(navArgument("productId"){ type = NavType.StringType})
        ) {
            Details(navController, it.arguments?.getString("productId"))
        }
    }
}

data class Product(
    val id:Int,
    val name:String
)

@Composable
fun ProductList(navController: NavHostController) {
    val productList = listOf(
        Product(233443, "Carne"),
        Product(344432, "Leche")
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = productList,
            key = { product -> product.id }
        ) { product ->
            ProductItem(
                productName = product.name,
                onClose = { navController.navigate("details/${product.id}") }
            )
        }
    }
}


@Composable
fun ProductItem(productName: String, onClose: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClose() }
            .padding(16.dp)
    ) {
        Text(text = productName)
    }
}

@Composable
fun Details(navController: NavHostController, productId:String?) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = productId ?: "No product")
    }
}
