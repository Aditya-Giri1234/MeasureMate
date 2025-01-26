package com.aditya.measuremate.domain.models.details

import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import com.google.firebase.Timestamp
import java.sql.Time
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class BodyPartValueDto(
    val bodyPartValueId : String?=null ,
    val bodyPartId : String?=null,
    val value : Float = 0f,
    val date : Timestamp  = Timestamp.now()
)

fun BodyPartValueDto.toBodyPartValue() : BodyPartValue{
    return BodyPartValue(
        value = this.value,
        date = this.date.toLocalDate() ,
        bodyPartId = this.bodyPartId ,
        bodyPartValueId = this.bodyPartValueId
    )
}

fun BodyPartValue.toBodyPartValueDto() : BodyPartValueDto{
    return BodyPartValueDto(
        value = this.value,
        date = this.date.toTimestamp() ,
        bodyPartId = this.bodyPartId ,
        bodyPartValueId = this.bodyPartValueId
    )
}

private fun Timestamp.toLocalDate() : LocalDate{
    return Instant.ofEpochSecond(seconds , nanoseconds.toLong()).atZone(ZoneId.systemDefault()).toLocalDate()
}

private fun LocalDate.toTimestamp() : Timestamp{
    val instant = this.atStartOfDay(ZoneId.systemDefault()).toInstant()
    return Timestamp(instant.toEpochMilli()/1000 , instant.nano%1000000)
}