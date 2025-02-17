package com.aditya.measuremate.domain.models.dashboard

import java.time.LocalDate

data class BodyPartValue(
    val bodyPartValueId : String?=null ,
    val bodyPartId : String?=null,
    val value : Float = 0f,
    val date : LocalDate = LocalDate.now()
)
