package com.aditya.measuremate.ui.compoenents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NewValueInputBar(
    modifier: Modifier = Modifier, date: String, value: String,
    isInputCardVisible : Boolean,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit ,
    onImeAction : KeyboardActions ,
    onCalenderClick : ()-> Unit
) {
    var inputError by rememberSaveable { mutableStateOf<String?>(null) }
    LaunchedEffect(value) {
        inputError = when{
            value.isBlank() -> "Please enter new value here."
            value.toFloatOrNull() == null -> "Invalid Number."
            value.toFloat() <1f ->"Please set at least 1."
            value.toFloat() > 1000f -> "Please set a maximum of 1000."
            else -> null
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = isInputCardVisible ,
        enter = slideInVertically { h -> h } ,
        exit = slideOutVertically { h -> h }
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(7.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number , imeAction = ImeAction.Done),
                keyboardActions = onImeAction,
                isError =  value.isNotBlank() && inputError != null,
                supportingText = {
                    Text(inputError.orEmpty())
                },
                trailingIcon = {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Text(date, style = MaterialTheme.typography.bodyMedium)
                        IconButton(onClick = {
                           onCalenderClick()
                        }) {
                            Icon(Icons.Filled.DateRange, contentDescription = null)
                        }
                    }
                }
            )

            FilledIconButton(
                onClick = {
                    onDone()
                } ,
                enabled = inputError == null
            ) {
                Icon(Icons.Filled.Done, contentDescription = null)
            }

        }
    }

}