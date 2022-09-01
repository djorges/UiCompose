package com.example.uicompose.data.util.compose.design

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true,
)
@Composable
fun GraphicsExample() {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawLine(
            start = Offset(x = canvasWidth, y = 0f),
            end = Offset(x = 0f, y = canvasHeight),
            color = Color.Blue
        )
        drawCircle(
            color = Color.Magenta,
            center = Offset(x = 0f, y = 0f),
            radius = size.minDimension/4
        )
        drawCircle(
            color = Color.Magenta,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = size.minDimension/4
        )
        drawCircle(
            color = Color.Magenta,
            center = Offset(x = canvasWidth, y = canvasHeight),
            radius = size.minDimension/4
        )
    }
}