package com.aditya.measuremate.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.ui.screen.AddItemScreen
import com.aditya.measuremate.ui.screen.DashboardScreen
import com.aditya.measuremate.ui.screen.DetailsScreen
import com.aditya.measuremate.ui.screen.SignInScreen
import com.aditya.measuremate.ui.viewmodels.SignInViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AppNavigation(
    windowWidthClass: WindowWidthSizeClass,
    innerPadding: PaddingValues,
    snackBarState: SnackbarHostState
) {
    val navController = rememberNavController()
    val signInViewModel : SignInViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        signInViewModel.uiEvent.onEach {
            when (it) {
                is UiEvent.SnackBar -> {
                    snackBarState.showSnackbar(it.msg)
                }

               else -> {}
            }
        }.launchIn(this)
    }

    NavHost(navController = navController , startDestination = AppNavigationScreens.DashboardScreen){
        composable<AppNavigationScreens.SignInScreen>(
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start , animationSpec = tween(500))
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End , animationSpec = tween(500))
            }
        ) {
            SignInScreen(navController , windowWidthClass , innerPadding = innerPadding , signInViewModel , snackBarState)
        }
        composable<AppNavigationScreens.DashboardScreen>(
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start , animationSpec = tween(500))
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End , animationSpec = tween(500))
            }
        ) {
            DashboardScreen(navController , windowWidthClass , paddingValues = innerPadding , hiltViewModel() , snackBarState)
        }
        composable<AppNavigationScreens.AddItemScreen>(
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start , animationSpec = tween(500))
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End , animationSpec = tween(500))
            }


        ) {
            AddItemScreen(navController , windowWidthClass , innerPadding = innerPadding , snackBarState , hiltViewModel())
        }
        composable<AppNavigationScreens.DetailsScreen>(
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Start , animationSpec = tween(500))
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.End , animationSpec = tween(500))
            }
        ) {
            val bodyPartId = it.toRoute<AppNavigationScreens.DetailsScreen>().bodyPartId
            DetailsScreen(navController , windowWidthClass , innerPadding = innerPadding , hiltViewModel() , snackBarState,bodyPartId)
        }
    }

}