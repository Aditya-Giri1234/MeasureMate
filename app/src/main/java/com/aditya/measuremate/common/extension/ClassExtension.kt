package com.example.common.extension

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt

//region:: other

fun Long.formatDate(): String{
    val dateTime = Date(this)
    return SimpleDateFormat("EEE,d MMM hh:mm aaa", Locale.getDefault()).format(dateTime)
}

fun String.urlEncode(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

fun String.urlDecode(): String {
    return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
}

fun Float.roundToDecimal(decimalPlaces: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlaces)
    return (this * multiplier).roundToInt() / multiplier.toFloat()
}

fun String.roundToDecimal(decimalPlaces: Int = 1): Float {
    val multiplier = 10.0.pow(decimalPlaces)
    val value = this.toFloatOrNull() ?: 0f
    return (value * multiplier).roundToInt() / multiplier.toFloat()
}

fun LocalDate?.changeLocalDateToGraphDate(
    defaultValue : LocalDate = LocalDate.now()
) : String{
    return try{
              this?.format(DateTimeFormatter.ofPattern("MMM dd")) ?: defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    }catch (e :Exception){
        e.printStackTrace()
         defaultValue.format(DateTimeFormatter.ofPattern("MMM dd"))
    }
}

//endregion