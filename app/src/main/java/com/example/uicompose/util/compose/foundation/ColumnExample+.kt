package com.example.uicompose.util.compose.foundation

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Example 1:
 * */
@Composable
fun ColumnExample(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Andre",
            modifier = Modifier
                .background(Color.Red)
        )
        Text(
            text = "Andre",
            modifier = Modifier
                .background(Color.Red)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize(),
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Andre",
            modifier = Modifier
                .background(Color.Red)
        )
        Text(
            text = "Andre",
            modifier = Modifier
                .background(Color.Red)
        )
    }
}


/**
 * Example 2:LazyColumn no indexed items
 * */
data class Message(
    val id:Int?=null
)
@Composable
fun MessageList(
    messages: List<Message> = List(15){id->
        Message(id)
    }
) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        //
        snapshotFlow { listState.firstVisibleItemIndex }//Emit Flow when state changes
            .map { index -> index > 0 }//except first item
            .distinctUntilChanged()//return flow when items change
            .collect {
                //
                Log.i("UICOMPOSE_APP","firstVisibleItemIndex changes")
            }
    }
    //Messages List Component
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ){
        items(
            items=messages
        ){ message ->
            MessageRow(message)
        }
    }

    //
}
@Composable
fun MessageRow(message: Message){
    Text(
        text = message.id.toString(),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        textAlign = TextAlign.Center
    )
}
/**
 * Example 3: LazyColumn indexed
 * */
@Composable
fun MessageList2(messages: List<Message>){
    LazyColumn{
        items(
            items = messages,
            key = { message ->
                message.id!!
            }
        ) { message ->
            MessageRow(message)
        }
    }
}

/**
 * Example 3: LazyGrid + animation
 * */

data class Photo(
    val id:Int?=null
)
@Preview(
    showBackground = true,
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoGrid(
    photos: List<Photo> = List(30) { i -> Photo(i) }

) {
    LazyVerticalGrid(
        modifier= Modifier.fillMaxSize(),
        cells = GridCells.Fixed(4),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){
        items(photos){ photo ->
            //Photo Item
            PhotoItem(
                photo,
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .animateItemPlacement()
            )
        }
    }
}

@Composable
fun PhotoItem(
    photo: Photo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = photo.id.toString())
        }
    }
}

/*
* TODO: https://developer.android.com/jetpack/compose/lists#large-datasets
* */
/**
 * Example : LazyVerticalGrid + paging
 * */
/*
@Preview(
    showBackground = true,
    widthDp = 400,
    heightDp = 800
)
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ListUsersScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewModel.listUsers.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            modifier= Modifier.fillMaxSize(),
            cells = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ){
            items(lazyPagingItems){ user ->
                //User Item
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(user?.avatar),
                        contentDescription = "User Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@ExperimentalFoundationApi
public fun <T: Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}
*/