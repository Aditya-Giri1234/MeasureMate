package com.aditya.measuremate.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aditya.measuremate.R
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.widgets.sign_in.AnonymousSignInButton
import com.aditya.measuremate.ui.widgets.sign_in.GoogleSignInButton
import com.example.udemycourseshoppingapp.ui.components.AddVerticalSpace

@PreviewScreenSizes
@Composable
fun SignInScreen(
    navController: NavController = rememberNavController(),
    screenSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact
) {

    var isSignInAnonymousDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    MeasureMetaDialog(
        isOpen = isSignInAnonymousDialogOpen,
        title = "Login anonymously ?",
        body = {
            Text("By logging in anonymously , you will not able to syncronize to the data across devices or after uninstalling the app. \n Are you sure you want to proceed?")
        },
        onDialogDismiss = {
            isSignInAnonymousDialogOpen = false
        },
        onConfirmButtonClick = {
            isSignInAnonymousDialogOpen = false
        }
    )

    Scaffold { innerPadding ->
        when (screenSize) {
            WindowWidthSizeClass.Compact -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.app_logo),
                        contentDescription = "",
                        modifier = Modifier.size(120.dp)
                    )
                    AddVerticalSpace(20)
                    Text("MeasureMate", style = MaterialTheme.typography.headlineLarge)
                    AddVerticalSpace(3)
                    Text(
                        "Measure progress , not perfection",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic
                        )
                    )
                    Spacer(modifier = Modifier.fillMaxHeight(.4f))

                    GoogleSignInButton(onClick = {

                    }, isLoading = false)
                    AddVerticalSpace(20)
                    AnonymousSignInButton(onClick = {
                        isSignInAnonymousDialogOpen = true
                    }, isLoading = false)
                }
            }

            else -> {
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.app_logo),
                            contentDescription = "",
                            modifier = Modifier.size(120.dp)
                        )
                        AddVerticalSpace(20)
                        Text("MeasureMate", style = MaterialTheme.typography.headlineLarge)
                        AddVerticalSpace(3)
                        Text(
                            "Measure progress , not perfection",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        GoogleSignInButton(onClick = {

                        }, isLoading = false)
                        AddVerticalSpace(20)
                        AnonymousSignInButton(onClick = {
                            isSignInAnonymousDialogOpen = true
                        }, isLoading = false)
                    }
                }
            }
        }
    }
}


