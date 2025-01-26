package com.aditya.measuremate.ui.event_state.details

import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import com.example.udemycourseshoppingapp.common.utils.helper.TimeRange
import java.time.LocalDate

data class DetailsState(
    val bodyPart: BodyPart ?=null,
    val textFieldValue : String = "" ,
    val date : LocalDate = LocalDate.now() ,
    val timeRange : TimeRange = TimeRange.LAST7DAYS ,
    val allBodyPartValue: List<BodyPartValue> = emptyList() ,
    val chartBodyPartValue : List<BodyPartValue>  = emptyList()
)
