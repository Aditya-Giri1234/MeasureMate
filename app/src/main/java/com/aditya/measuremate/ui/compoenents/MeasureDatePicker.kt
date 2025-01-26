package com.aditya.measuremate.ui.compoenents

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeasureMateDatePicker(modifier: Modifier = Modifier , datePickerState : DatePickerState  , isOpen : Boolean, confirmButtonTExt : String = "OK" , dismissButtonText : String= "Cancel" , onDismissRequest : () -> Unit , onConfirmButton :  ()-> Unit) {

    if(isOpen){
        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = {
                onDismissRequest()
            } ,
            confirmButton = {
                Text(confirmButtonTExt)
            } ,
            dismissButton = {
                Text(dismissButtonText)
            } ,
            content = {
                DatePicker(state = datePickerState )
            }
        )
    }

}