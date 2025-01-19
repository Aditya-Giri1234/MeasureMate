package com.aditya.myrecipe.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ErrorText(msg: String, modifier: Modifier = Modifier) {
    Text(
        msg, style = MaterialTheme.typography.titleMedium.copy(
            color = Color.Red.copy(
                alpha = .9f
            ),
            fontWeight = FontWeight.Medium
        ), modifier = modifier ,
        textAlign = TextAlign.Center
    )
}