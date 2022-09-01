package com.example.uicompose.data.util.compose.adopting

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview

/**
 * Onclick label
 *
 */
@Composable
fun AccessibilityExample(
    openArticle: () -> Unit,
    openArticles: () -> Boolean
){
    Row(
        modifier = Modifier
            .clickable(onClickLabel = "Read Article", onClick = openArticle)
    ) {
        Text(text = "This article is interesting")
    }

    Canvas(
        modifier = Modifier.semantics {
            onClick(label = "Read Article", action = openArticles)
        },
    ){
        //To draw something
    }
}

/**
 * Image contentDescription
 */
@Preview(
    showBackground = true
)
@Composable
fun ShareButton(
    onClick: () -> Unit = {}
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = "Share Icon"
        )
    }
}

/**
 * Example: Merge elements(element focus)
 *
 * */
@Composable
private fun PostMetadata(
    metadata: Metadata
) {
    // Merge elements below for accessibility purposes
    /*Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Image(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = null // decorative
        )
        Column {
            Text(metadata.author.name)
            Text("${metadata.date} • ${metadata.readTimeMinutes} min read")
        }
    }*/
}
