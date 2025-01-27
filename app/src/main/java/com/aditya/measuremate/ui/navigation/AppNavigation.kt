package com.aditya.measuremate.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.ui.event_state.sign.SignInState
import com.aditya.measuremate.ui.screen.AddItemScreen
import com.aditya.measuremate.ui.screen.DashboardScreen
import com.aditya.measuremate.ui.screen.DetailsScreen
import com.aditya.measuremate.ui.screen.SignInScreen
import com.aditya.measuremate.ui.viewmodels.AddItemViewModel
import com.aditya.measuremate.ui.viewmodels.DashboardViewModel
import com.aditya.measuremate.ui.viewmodels.DetailsViewModel
import com.aditya.measuremate.ui.viewmodels.SignInViewModel
import com.example.udemycourseshoppingapp.common.utils.helper.AuthState
import com.example.udemycourseshoppingapp.common.utils.helper.MyLogger
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AppNavigation(
    windowWidthClass: WindowWidthSizeClass,
    innerPadding: PaddingValues,
    snackBarState: SnackbarHostState
) {
    val navController = rememberNavController()
    val signInViewModel: SignInViewModel = hiltViewModel()
    val authState by signInViewModel.authState.collectAsStateWithLifecycle()
    var previousAuthStatus by rememberSaveable { mutableStateOf<AuthState?>(null) }

    LaunchedEffect(authState) {
        if (previousAuthStatus != authState) {
            when(authState){
                AuthState.Loading -> {}
                AuthState.Authorized -> {
                    navController.navigate(AppNavigationScreens.DashboardScreen){
                        popUpTo(0)
                    }
                }
                AuthState.UnAuthorized -> {
                    navController.navigate(AppNavigationScreens.SignInScreen){
                        popUpTo(0)
                    }
                }
            }
            previousAuthStatus = authState
        }

    }

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

    NavHost(
        modifier = Modifier.padding(paddingValues = innerPadding),
        navController = navController,
        startDestination = AppNavigationScreens.DashboardScreen
    ) {
        composable<AppNavigationScreens.SignInScreen> {
            val signInState = signInViewModel.signInState.collectAsStateWithLifecycle(SignInState())
            SignInScreen(
                navController,
                windowWidthClass,
                signInState,
                signInViewModel::onEvent
            )
        }
        composable<AppNavigationScreens.DashboardScreen>{
            val dashboardViewModel = hiltViewModel<DashboardViewModel>()
            val dashBoardState =
                dashboardViewModel.dashBoardScreenState.collectAsStateWithLifecycle()
            DashboardScreen(
                navController,
                windowWidthClass,
                snackBarState,
                dashBoardState,
                dashboardViewModel.uiEvent,
                dashboardViewModel::onEvent
            )
        }
        composable<AppNavigationScreens.AddItemScreen>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                )
            }


        ) {
            val addItemViewModel = hiltViewModel<AddItemViewModel>()
            val uiScreenState = addItemViewModel.uiScreenState.collectAsStateWithLifecycle()

            AddItemScreen(navController, windowWidthClass , snackBarState ,uiScreenState , addItemViewModel.uiEvent , addItemViewModel::onEvent )
        }
        composable<AppNavigationScreens.DetailsScreen>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(500)
                )
            }
        ) {
            val bodyPartId = it.toRoute<AppNavigationScreens.DetailsScreen>().bodyPartId
            val detailViewModel = hiltViewModel<DetailsViewModel>()
            val detailsState = detailViewModel.uiScreenState.collectAsStateWithLifecycle()
            DetailsScreen(
                navController,
                windowWidthClass,
                snackBarState,
                bodyPartId ,
                detailsState ,
                detailViewModel.uiEvent ,
                detailViewModel::onEvent
            )
        }
    }

}