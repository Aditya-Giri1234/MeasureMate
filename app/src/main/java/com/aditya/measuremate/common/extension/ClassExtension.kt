package com.example.common.extension

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

//endregion