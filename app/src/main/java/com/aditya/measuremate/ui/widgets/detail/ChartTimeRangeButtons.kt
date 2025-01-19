package com.aditya.measuremate.ui.widgets.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.udemycourseshoppingapp.common.utils.helper.TimeRange

@Composable
fun ChartTimeRangeButtons(
    modifier: Modifier = Modifier,
    selectedRangeButton: TimeRange,
    onClick: (TimeRange) -> Unit
) {
    Row(
        modifier
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        TimeRange.entries.forEach { timeRange ->
            TimeRangeSelectionButton(
                modifier = Modifier.weight(1f),
                label = timeRange.name ,
                labelTextStyle = if(timeRange == selectedRangeButton){
                    MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                }else{
                    MaterialTheme.typography.labelLarge.copy(color = Color.Gray)
                } ,
                backgroundColor = if(timeRange == selectedRangeButton){
                    MaterialTheme.colorScheme.surface
                }else{
                   Color.Transparent
                } ,
                onClick = {onClick(timeRange)}
            )
        }
    }


}