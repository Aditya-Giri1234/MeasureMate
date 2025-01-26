package com.aditya.measuremate.domain.models.common

data class User(
    val name : String = "",
    val email : String = "",
    val profilePictureUrl : String = "",
    @field:JvmField
    val isAnonymous : Boolean = true,
    val userId : String?=null
)
