package com.aditya.measuremate.common.models
sealed class ApiResponse<T>(open val data : T?=null , open val message :String?=null) {
    class Initial<T>() : ApiResponse<T>()
    class Loading<T>() : ApiResponse<T>()
    data class Success<T>(override val data: T) : ApiResponse<T>(data){
        var isMessageSeen : Boolean = false
    }
    data class Error<T>(override val message: String) : ApiResponse<T>(message = message){
        var isMessageSeen : Boolean = false
    }
}