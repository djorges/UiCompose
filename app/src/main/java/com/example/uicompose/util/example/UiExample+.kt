package com.example.uicompose.util.example

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Medium
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uicompose.R
import com.example.uicompose.presentation.ui.theme.Shapes

private val AppBarHeight = 200.dp

/**
 * Example: Ui with Compose(youtube)
 *
 * */
@Preview(
    name = "First screen",
    showBackground = true,
    widthDp = 600,
    heightDp = 1200
)
@Composable
fun MainFragment(){
    val movie = Movie(
        id=1,
        title="Title",
        category="Category",
        rating=5.6f,
        durationMil=234499,
        year = 2015
    )
    Box{
        Content(movie)

        TopBar(movie)
    }
}

@Composable
fun Content(movie: Movie){
    //Main Content
    LazyColumn(
        contentPadding = PaddingValues(top = AppBarHeight)
    ){
        item{
            MovieInfo(movie)
            Description(movie)
        }
    }
}

@Composable
fun Description(movie: Movie){
    Text(
        text = movie.description!!,
        fontWeight = Medium,
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 16.dp
        )
    )
}

@Composable
fun ServingCalculator(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 8.dp
        )
    ){
        Text(text = "Serving")
    }
}

@Composable
fun MovieInfo(movie: Movie){
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        IconInfo(R.drawable.ic_baseline_movie_24, movie.year.toString())
        IconInfo(R.drawable.ic_baseline_access_time_24, movie.durationMil.toString())
        IconInfo(R.drawable.ic_baseline_star_outline_24, movie.rating.toString())
    }
}

@Composable
fun IconInfo(
    @DrawableRes iconResource: Int,
    text:String
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = "Icon",
            tint = Color.Red,
            modifier = Modifier.height(24.dp)
        )
        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TopBar(movie: Movie){
    //TopAppBar Content
    TopAppBar(
        contentPadding = PaddingValues(),
        backgroundColor = White,
        modifier = Modifier
            .height(AppBarHeight)
    ) {
        Column{
            Box(
                modifier = Modifier.height(400.dp)
            ) {
                //Image
                Image(
                    painter = painterResource(id = R.drawable.wallpaper),
                    contentDescription = "Main Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
                //Gradient Background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colorStops = arrayOf(
                                    Pair(
                                        0.2f,
                                        Transparent
                                    ), Pair(1f, White)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(
                            horizontal = 20.dp,
                            vertical = 40.dp
                        ),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.Start
                ){
                    //Category
                    Text(
                        text = movie.category!!,
                        modifier = Modifier
                            .clip(Shapes.small)
                            .background(Color.LightGray)
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                    )
                    //Title
                    Text(
                        text = movie.title!!,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    //Action Buttons
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp)
    ){
        CircularButton(
            iconResource = R.drawable.ic_baseline_arrow_left_24,
            onClickListener = {
                /*TODO*/
            }
        )
        CircularButton(
            iconResource = R.drawable.ic_baseline_favorite_24,
            onClickListener = {
                /*TODO*/
            }
        )
    }
}

@Composable
fun CircularButton(
    @DrawableRes iconResource: Int,
    color: Color = Gray,
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    onClickListener: () -> Unit = {}
){
    Button(
        onClick = onClickListener,
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(backgroundColor = White, contentColor = color),
        elevation = elevation,
        modifier = Modifier
            .height(38.dp)
            .width(38.dp)
    ){
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null
        )
    }
}

data class Movie(
    val id: Int? = null,
    val title: String? = null,
    val category: String? = null,
    val rating:Float? = null,
    val year: Int? = null,
    val durationMil: Int? = null,
    val description: String? = null
)