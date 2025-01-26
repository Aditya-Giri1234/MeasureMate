package com.aditya.measuremate.ui.event.details

import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import com.example.udemycourseshoppingapp.common.utils.helper.MeasuringUnit
import com.example.udemycourseshoppingapp.common.utils.helper.TimeRange

sealed class DetailsEvent {
    data object DeleteBodyPart : DetailsEvent()
    data object RestoreBodyPartValue : DetailsEvent()
    data object AddNewValue : DetailsEvent()
    data class DeleteBodyPartValue(val bodyPartValue : BodyPartValue) : DetailsEvent()
    data class ChangeMeasuringUnit(val measuringUnit: MeasuringUnit) : DetailsEvent()
    data class OnDateChange(val millis : Long?) : DetailsEvent()
    data class OnTextFieldValueChange(val value : String) : DetailsEvent()
    data class OnTimeRangeChange(val timeRange : TimeRange) : DetailsEvent()
}