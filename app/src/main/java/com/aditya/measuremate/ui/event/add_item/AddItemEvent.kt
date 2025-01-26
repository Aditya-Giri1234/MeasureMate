package com.aditya.measuremate.ui.event.add_item

import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.ui.event_state.add_item.AddItemState

sealed class AddItemEvent {
    data class OnTextFieldValueChange(val value : String) : AddItemEvent()
    data class OnItemClick(val bodyPart : BodyPart) : AddItemEvent()
    data class OnItemIsActiveChanged(val bodyPart: BodyPart) : AddItemEvent()
    data object OnAddItemDialogDismiss : AddItemEvent()
    data object UpsertItem : AddItemEvent()
}