package com.aditya.measuremate.ui.event_state.add_item

import com.aditya.measuremate.domain.models.dashboard.BodyPart
import retrofit2.http.Body

data class AddItemState(
    val textFieldValue : String = "" ,
    val selectedBodyPart : BodyPart ?= null,
    val bodyParts : List<BodyPart> = emptyList()
)
