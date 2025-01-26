package com.aditya.measuremate.ui.event_state.sign

data class SignInState(
    val isGoogleSignInButtonLoading : Boolean = false,
    val isAnonymousSignInButtonLoading : Boolean = false
)
