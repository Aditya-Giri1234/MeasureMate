package com.aditya.measuremate.ui.event_state.dashboard

import com.aditya.measuremate.domain.models.common.User
import com.aditya.measuremate.domain.models.dashboard.BodyPart

data class DashboardState(
    val user: User? = null,
    val bodyPart : List<BodyPart> = emptyList(),
    val isSignOutButtonLoading : Boolean = false,
    val isSignInButtonLoading : Boolean = false
)