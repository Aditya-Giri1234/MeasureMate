package com.example.udemycourseshoppingapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aditya.measuremate.ui.theme.CustomBlue
import com.aditya.measuremate.ui.theme.CustomPink
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    title: String = "ShoppingApp",
    screenName: AppScreenName,
    navController: NavController,
    action : @Composable RowScope.()-> Unit
) {

    TopAppBar(
        title = {
            Text(
                title, style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                ) , maxLines = 1 , overflow = TextOverflow.Ellipsis
            )
        },
        windowInsets = WindowInsets(left = 0, top = 0 , right = 0 , bottom = 0),
        navigationIcon = {
            NavigationIcon(screenName, title, navController)
        },
        actions = action,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.LightGray
        )
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NavigationIcon(
    screenName: AppScreenName,
    title: String,
    navController: NavController
) {
    when (screenName) {
        AppScreenName.HomeScreen -> {

        }

        else -> {
            IconWithoutDesc(
                Icons.AutoMirrored.Filled.ArrowBack,
                tint = Color.Black,
                modifier = Modifier.clickable {
                    navController.navigateUp()
                })
        }
    }

}