package com.aditya.measuremate.ui.event.sign

import android.content.Context

sealed class SignInEvent {

    data class SignInWithGoogle(val context : Context) : SignInEvent()

    data object SignInAnonymously : SignInEvent()

}