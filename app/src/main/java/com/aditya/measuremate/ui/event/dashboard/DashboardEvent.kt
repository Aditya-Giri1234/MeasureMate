package com.aditya.measuremate.ui.event.dashboard

import android.content.Context

sealed class DashboardEvent {
    data class AnonymouslyUserSignInGoogle(val context : Context) : DashboardEvent()
    data object  SignOut : DashboardEvent()
}