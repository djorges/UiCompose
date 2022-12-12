package com.example.uicompose.util.compose.foundation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uicompose.R


/**
 *
 * */
@Preview(
    showBackground = true,
    widthDp = 400,
    heightDp = 100
)
@Composable
fun ArtistCard(/*...*/) {
    Row(
        modifier = Modifier
            .size(width = 400.dp, height = 100.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_up_rating_24),
            contentDescription= "Up Rating",
            modifier = Modifier
                .weight(1f)
        )
        Column(
            modifier = Modifier
                .weight(2f)
        ) {
            Text(
                text="Title",
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text="Title"
            )
        }
    }
}