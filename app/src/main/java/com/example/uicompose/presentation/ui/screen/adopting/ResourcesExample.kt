package com.example.uicompose.presentation.ui.screen.adopting

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uicompose.R

@Preview(
    showBackground = true,
    name = "Resource Example",
    widthDp = 300,
    heightDp = 300
)
@Composable
fun ResourcesExampleScreen(){
    Text(
        text=stringResource(id = R.string.app_name),
        fontSize=20.sp,
        textAlign= TextAlign.Center,
        color=colorResource(id = R.color.purple_500),
        modifier=Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    )
}