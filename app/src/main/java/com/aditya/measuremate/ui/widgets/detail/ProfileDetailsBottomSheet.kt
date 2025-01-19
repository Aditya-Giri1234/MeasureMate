package com.aditya.measuremate.ui.widgets.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.udemycourseshoppingapp.common.utils.helper.MeasuringUnit
import com.example.udemycourseshoppingapp.ui.components.AddVerticalSpace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    isBottomSheetOpen: Boolean,
    onBottomSheetDismiss: () -> Unit,
    onItemClick: (MeasuringUnit) -> Unit
) {

    if (isBottomSheetOpen) {
        ModalBottomSheet(
            modifier = modifier,
            sheetState = sheetState,
            onDismissRequest = onBottomSheetDismiss,
            dragHandle = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text("Select Measuring Unit", style = MaterialTheme.typography.titleLarge)
                    AddVerticalSpace(10)
                    HorizontalDivider()
                }
            }
        ) {
            AddVerticalSpace(12)
            MeasuringUnit.entries.forEach { unit ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(unit)
                        }
                        .padding(vertical = 12.dp, horizontal = 20.dp)
                ) {
                    Text("${unit.code} ${unit.label}", style = MaterialTheme.typography.bodyLarge)
                }
            }
            AddVerticalSpace(12)
        }
    }

}