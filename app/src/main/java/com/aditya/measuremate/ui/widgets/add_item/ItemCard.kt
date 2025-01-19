package com.aditya.measuremate.ui.widgets.add_item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.aditya.measuremate.domain.models.dashboard.BodyPart

@Composable
fun AddItemCard(
    modifier: Modifier = Modifier,
    name : String,
    isChecked: Boolean,
    onCheckChange: () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.clickable {
            onClick()
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(.8f),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    onCheckChange()
                }
            )
        }
    }
}