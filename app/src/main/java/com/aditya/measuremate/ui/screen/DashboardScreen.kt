package com.aditya.measuremate.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aditya.measuremate.domain.models.common.User
import com.aditya.measuremate.domain.models.dashboard.predefinedBodyParts
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.theme.CustomBlue
import com.aditya.measuremate.ui.theme.CustomPink
import com.aditya.measuremate.ui.widgets.dashboard.ItemCard
import com.aditya.measuremate.ui.widgets.dashboard.ProfileDetailsBottomSheet
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName
import com.example.udemycourseshoppingapp.ui.components.CircleImageLoading
import com.example.udemycourseshoppingapp.ui.components.MyTopBar

@Preview
@Composable
fun DashboardScreen(
    navController: NavController = rememberNavController(),
    windowSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact
) {
    var isBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    val user = remember {
        User(
            name = "Aditya Giri" ,
            email = "aditya@gmail.com" ,
            profilePictureUrl = "https://plus.unsplash.com/premium_photo-1664474619075-644dd191935f?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aW1hZ2V8ZW58MHx8MHx8fDA%3D" ,
            isAnonymous = false
        )
    }

    var isSignOutDialogOpen by rememberSaveable {
        mutableStateOf(false)
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
        }
    )

    if(isBottomSheetOpen){
        ProfileDetailsBottomSheet(
            isBottomSheetOpen = isBottomSheetOpen,
            user = user ,
            onBottomSheetDismiss = {
                isBottomSheetOpen = false
            } ,
            primaryText = "Sign out from Google" ,
            buttonLoadingState = false ,
            onGoogleButtonClick = {
                isSignOutDialogOpen = true
            }

        )
    }


    Scaffold(
        topBar = {
            MyTopBar(
                screenName = AppScreenName.DashboardScreen,
                title = "Dashboard Screen",
                navController = navController,
                action = {
                    IconButton(onClick = {
                        isBottomSheetOpen = true
                    }) {
                        CircleImageLoading(
                            user.profilePictureUrl ?: "", modifier = Modifier
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
        } ,
        floatingActionButton = {
            FloatingActionButton(onClick = {

            } , shape = CircleShape) {
                Icon(Icons.Filled.Add , contentDescription = "")
            }
        } ,
        floatingActionButtonPosition = FabPosition.End
    )
    { innerPadding ->

        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(predefinedBodyParts) { item ->
                ItemCard(bodyPart = item)
            }
        }

    }

}