package com.aditya.measuremate.ui.widgets.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aditya.measuremate.R
import com.example.udemycourseshoppingapp.ui.components.AddHorizontalSpace

@Preview
@Composable
fun GoogleSignInButton(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    primaryText: String = "Sign in to google",
    secondaryText: String = "Please wait...",
    onClick: () -> Unit = {}
) {
    var buttonText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(isLoading) {
        buttonText = if(isLoading) secondaryText else primaryText
    }
    Button(
        onClick = onClick ,
        enabled = !isLoading ,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground ,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {

        if(!isLoading){
            Icon(
                painter = painterResource(R.drawable.ic_google_logo) , contentDescription = "" , tint = Color.Unspecified
            )
        }
        AddHorizontalSpace(5)
        Text(buttonText)
        AddHorizontalSpace(5)
        if(isLoading){
            CircularProgressIndicator(strokeWidth = 1.dp, color = Color.White , modifier = Modifier.size(20.dp))
        }

    }
}