package com.aditya.measuremate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.widgets.detail.ChartTimeRangeButtons
import com.aditya.measuremate.ui.widgets.detail.DetailsBottomSheet
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName
import com.example.udemycourseshoppingapp.common.utils.helper.MeasuringUnit
import com.example.udemycourseshoppingapp.common.utils.helper.TimeRange
import com.example.udemycourseshoppingapp.ui.components.AddHorizontalSpace
import com.example.udemycourseshoppingapp.ui.components.MyTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController, bodyPart: BodyPart = BodyPart(
        name = "Waist",
        isActive = true,
        measuringUnit = MeasuringUnit.CM.code
    )
) {

    var isDeleteItemDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isDetailBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    var selectedCharTimeRangeButton by rememberSaveable {
        mutableStateOf(TimeRange.LAST7DAYS)
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    DetailsBottomSheet(
        sheetState = sheetState,
        isBottomSheetOpen = isDetailBottomSheet,
        onBottomSheetDismiss = {
            isDetailBottomSheet = false
        },
        onItemClick = {
            scope.launch {
                sheetState.hide()
            }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    isDetailBottomSheet = false
                }
            }
        }
    )

    MeasureMetaDialog(
        isOpen = isDeleteItemDialogOpen,
        title = "Delete Item",
        confirmButtonText = "Delete",
        body = {
            Text("Are you sure to delete this item ?. This action can not be undone.")
        },
        onDialogDismiss = {
            isDeleteItemDialogOpen = false
        },
        onConfirmButtonClick = {
            isDeleteItemDialogOpen = false
        }
    )

    Scaffold(
        topBar = {
            MyTopBar(
                screenName = AppScreenName.DetailsScreen,
                title = bodyPart.name,
                navController = navController,
                action = {
                    IconButton(onClick = {
                        isDeleteItemDialogOpen = true
                    }) {
                        Icon(Icons.Rounded.Delete, contentDescription = "")
                    }
                    AddHorizontalSpace(4)
                    Text(bodyPart.measuringUnit)
                    AddHorizontalSpace(5)
                    IconButton(onClick = {
                        isDetailBottomSheet = true
                    }) {
                        Icon(Icons.Rounded.ArrowDownward, contentDescription = "")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            ChartTimeRangeButtons(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                selectedRangeButton = selectedCharTimeRangeButton,
                onClick = {
                    selectedCharTimeRangeButton = it
                })
        }

    }

}