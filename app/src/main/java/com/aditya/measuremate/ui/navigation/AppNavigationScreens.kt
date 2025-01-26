package com.aditya.measuremate.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavigationScreens {

    @Serializable
    data object  SignInScreen : AppNavigationScreens

    @Serializable
    data object DashboardScreen : AppNavigationScreens

    @Serializable
    data object AddItemScreen : AppNavigationScreens

    @Serializable
    data class DetailsScreen(val bodyPartId : String) : AppNavigationScreens
}