package com.aditya.measuremate.common.utils

sealed class UiEvent {
    data class SnackBar(val msg : String , val actionLabel : String?=null) : UiEvent()
    data object HideBottomSheet : UiEvent()
    data object Navigate : UiEvent()
}