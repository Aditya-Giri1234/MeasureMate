package com.aditya.measuremate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.aditya.measuremate.ui.screen.AddItemScreen
import com.aditya.measuremate.ui.screen.DashboardScreen
import com.aditya.measuremate.ui.screen.DetailsScreen
import com.aditya.measuremate.ui.screen.SignInScreen
import com.aditya.measuremate.ui.theme.MeasureMateTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeasureMateTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
               MyApp(windowSizeClass.widthSizeClass)
            }
        }
    }
}

@Composable
fun MyApp(screenSize: WindowWidthSizeClass) {
//    SignInScreen(rememberNavController() , screenSize)
//    DashboardScreen(rememberNavController() , screenSize)
//    AddItemScreen(rememberNavController() , screenSize)
    DetailsScreen(rememberNavController())
}