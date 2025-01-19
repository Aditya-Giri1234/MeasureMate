package com.example.udemycourseshoppingapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddVerticalSpace(margin: Int){
    Spacer(modifier = Modifier.height(margin.dp))
}

@Composable
fun AddHorizontalSpace(margin: Int){
    Spacer(modifier = Modifier.width(margin.dp))
}

@Composable
fun ShowCircularProgressBar(modifier : Modifier = Modifier.fillMaxSize() , contentAlignment : Alignment = Alignment.Center){
    Box(modifier = modifier , contentAlignment = contentAlignment) {
        CircularProgressIndicator(modifier = Modifier
            .size(50.dp))
    }
}

@Composable
fun ShowHorizontalDottedLine(pathEffect : PathEffect, color: Color){
    Canvas(modifier = Modifier.fillMaxWidth()) {
        drawLine(color = color , start = Offset(0f,0f) , end = Offset(size.width , 0f) , pathEffect = pathEffect)
    }
}




@Composable
@ExperimentalMaterial3Api
fun IconWithoutDesc(
    imageVector: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector,
        "",
        modifier,
        tint
    )
}

@Composable
@ExperimentalMaterial3Api
fun IconWithoutDesc(
    bitmap: ImageBitmap,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Icon(
        bitmap,
        "",
        modifier,
        tint
    )
}



@Composable
fun MyTextField(modifier: Modifier = Modifier , placeLabel : String  , text : MutableState<String> , keyboardOptions: KeyboardOptions ,keyboardActions: KeyboardActions = KeyboardActions {  }) {

   OutlinedTextField(
       value = text.value ,
       onValueChange = {
           text.value = it
       } ,
       modifier = modifier,
       keyboardOptions = keyboardOptions ,
       keyboardActions = keyboardActions ,
       placeholder = {
           Text(placeLabel , style = MaterialTheme.typography.bodyLarge.copy(
               color = Color.Gray.copy(
                   alpha = .6f
               )
           ))
       } ,
       label = {
           Text(placeLabel , style = MaterialTheme.typography.bodyLarge.copy(
               color = Color.Gray.copy(
                   alpha = .6f
               )
           ))
       } ,
       colors = TextFieldDefaults.colors(
           focusedTextColor = Color.Black ,
           unfocusedTextColor = Color.Gray ,
           focusedContainerColor = Color.White ,
           unfocusedContainerColor = Color.White ,
           focusedIndicatorColor = Color.Blue ,
           unfocusedIndicatorColor = Color.Gray
       )
   )

}