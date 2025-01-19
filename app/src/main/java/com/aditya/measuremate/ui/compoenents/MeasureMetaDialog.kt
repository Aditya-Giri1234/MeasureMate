package com.aditya.measuremate.ui.compoenents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun MeasureMetaDialog(
    isOpen: Boolean,
    title: String,
    confirmButtonText : String  = "Yes",
    body: @Composable () -> Unit,
    onDialogDismiss: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {

    if (isOpen) {
        AlertDialog(onDismissRequest = {
            onDialogDismiss()
        },
            title = {
                Text(title , style = MaterialTheme.typography.headlineLarge)
            }, text = body, confirmButton = {
                TextButton(onClick = onConfirmButtonClick) {
                    Text(confirmButtonText)
                }
            }, dismissButton = {
                TextButton(onClick = onDialogDismiss) {
                    Text("Cancel")
                }
            })
    }

}