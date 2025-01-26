package com.aditya.measuremate.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.event.dashboard.DashboardEvent
import com.aditya.measuremate.ui.navigation.AppNavigationScreens
import com.aditya.measuremate.ui.theme.CustomBlue
import com.aditya.measuremate.ui.theme.CustomPink
import com.aditya.measuremate.ui.viewmodels.DashboardViewModel
import com.aditya.measuremate.ui.widgets.dashboard.ItemCard
import com.aditya.measuremate.ui.widgets.dashboard.ProfileDetailsBottomSheet
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName
import com.example.udemycourseshoppingapp.common.utils.helper.AuthState
import com.example.udemycourseshoppingapp.ui.components.AddVerticalSpace
import com.example.udemycourseshoppingapp.ui.components.CircleImageLoading
import com.example.udemycourseshoppingapp.ui.components.MyTopBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DashboardScreen(
    navController: NavController = rememberNavController(),
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    paddingValues: PaddingValues = PaddingValues(8.dp),
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    snackBarState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val authState = dashboardViewModel.authState.collectAsStateWithLifecycle()
    val dashBoardState = dashboardViewModel.dashBoardScreenState.collectAsStateWithLifecycle()

    var isSignOutDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val isUserAnonymous = dashBoardState.value.user?.isAnonymous ?: true

    LaunchedEffect(Unit) {
        dashboardViewModel.uiEvent.onEach {
            when (it) {
                is UiEvent.SnackBar -> {
                    snackBarState.showSnackbar(it.msg)
                }

                UiEvent.HideBottomSheet -> {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            isBottomSheetOpen = false
                        }
                    }
                }

                UiEvent.Navigate -> {}
            }
        }.launchIn(this)
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            AuthState.UnAuthorized -> {
                navController.navigate(AppNavigationScreens.SignInScreen) {
                    popUpTo(AppNavigationScreens.SignInScreen) {
                        inclusive = false
                    }
                }
            }

            else -> {}
        }
    }

    MeasureMetaDialog(
        isOpen = isSignOutDialogOpen,
        title = "Sign Out",
        body = {
            Text("Are you sure to sign out?")
        },
        onDialogDismiss = {
            isSignOutDialogOpen = false
        },
        onConfirmButtonClick = {
            isSignOutDialogOpen = false
            dashboardViewModel.onEvent(DashboardEvent.SignOut)
        }
    )

    ProfileDetailsBottomSheet(
        isBottomSheetOpen = isBottomSheetOpen,
        user = dashBoardState
            .value.user,
        sheetState = sheetState,
        onBottomSheetDismiss = {
            isBottomSheetOpen = false
        },
        primaryText = if (isUserAnonymous) "Sign in with Google" else "Sign out from Google",
        buttonLoadingState = if (isUserAnonymous) dashBoardState.value.isSignInButtonLoading else dashBoardState.value.isSignOutButtonLoading,
        onGoogleButtonClick = {
            if (isUserAnonymous) dashboardViewModel.onEvent(
                DashboardEvent.AnonymouslyUserSignInGoogle(
                    context = context
                )
            ) else
                isSignOutDialogOpen = true
        }

    )



    Box(
        modifier = Modifier.padding(paddingValues)
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            MyTopBar(
                screenName = AppScreenName.DashboardScreen,
                title = "Dashboard Screen",
                navController = navController,
                action = {
                    IconButton(onClick = {
                        isBottomSheetOpen = true
                    }) {
                        CircleImageLoading(
                            dashBoardState.value.user?.profilePictureUrl, modifier = Modifier
                                .size(120.dp)
                                .border(
                                    width = 2.dp,
                                    brush = Brush.linearGradient(listOf(CustomBlue, CustomPink)),
                                    shape = CircleShape
                                )
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )
            AddVerticalSpace(10)
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(dashBoardState.value.bodyPart) { item ->
                    ItemCard(bodyPart = item, onItemClick = {
                        it.bodyPartId?.let {
                            navController.navigate(
                                AppNavigationScreens.DetailsScreen(
                                    it
                                )
                            )
                        }
                    })
                }
            }

        }

        FloatingActionButton(
            onClick = {
                navController.navigate(AppNavigationScreens.AddItemScreen)
            }, shape = CircleShape, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "")
        }

    }


}