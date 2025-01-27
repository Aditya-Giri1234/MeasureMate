package com.aditya.measuremate.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.common.utils.custom_class.MyCalenderSelectableDate
import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import com.aditya.measuremate.ui.compoenents.MeasureMateDatePicker
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.compoenents.NewValueInputBar
import com.aditya.measuremate.ui.event.details.DetailsEvent
import com.aditya.measuremate.ui.event_state.details.DetailsState
import com.aditya.measuremate.ui.widgets.detail.ChartTimeRangeButtons
import com.aditya.measuremate.ui.widgets.detail.DetailsBottomSheet
import com.aditya.measuremate.ui.widgets.detail.LineGraph
import com.example.common.extension.changeLocalDateToDateString
import com.example.common.extension.changeToDate
import com.example.common.extension.roundToDecimal
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName
import com.example.udemycourseshoppingapp.common.utils.helper.MeasuringUnit
import com.example.udemycourseshoppingapp.ui.components.AddHorizontalSpace
import com.example.udemycourseshoppingapp.ui.components.AddVerticalSpace
import com.example.udemycourseshoppingapp.ui.components.MyTopBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController = rememberNavController(),
    windowWidthSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    snackBarState: SnackbarHostState = remember { SnackbarHostState() },
    bodyPartId: String = "541",
    detailsState: State<DetailsState>,
    uiEvent: Flow<UiEvent>,
    onEvent: (DetailsEvent) -> Unit,
) {


    var isInputCardVisible by rememberSaveable {
        mutableStateOf(true)
    }
    var isDeleteItemDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isDetailBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    var isDatePickerDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = MyCalenderSelectableDate
    )
    val scope = rememberCoroutineScope()
    val focusController = LocalFocusManager.current

    LaunchedEffect(Unit) {
        uiEvent.onEach {
            when (it) {
                is UiEvent.SnackBar -> {
                    val action = snackBarState.showSnackbar(
                        it.msg,
                        actionLabel = it.actionLabel,
                        duration = SnackbarDuration.Short
                    )

                    if (action == SnackbarResult.ActionPerformed) {
                        onEvent(DetailsEvent.RestoreBodyPartValue)
                    }

                }

                UiEvent.HideBottomSheet -> {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        if (!sheetState.isVisible) {
                            isDetailBottomSheet = false
                        }
                    }
                }

                UiEvent.Navigate -> {
                    navController.navigateUp()
                }
            }
        }.launchIn(this)
    }

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
            onEvent(DetailsEvent.ChangeMeasuringUnit(it))
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
            onEvent(DetailsEvent.DeleteBodyPart)
        }
    )

    MeasureMateDatePicker(
        datePickerState = dateState,
        isOpen = isDatePickerDialogOpen,
        onDismissRequest = { isDatePickerDialogOpen = false },
        onConfirmButton = {
            isDatePickerDialogOpen = false
            onEvent(DetailsEvent.OnDateChange(dateState.selectedDateMillis))
        }
    )

    when (windowWidthSize) {
        WindowWidthSizeClass.Compact -> {
            Box(
                Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MyTopBar(
                        screenName = AppScreenName.DetailsScreen,
                        title = detailsState.value.bodyPart?.name ?: "",
                        navController = navController,
                        action = {
                            IconButton(onClick = {
                                isDeleteItemDialogOpen = true
                            }) {
                                Icon(Icons.Rounded.Delete, contentDescription = "")
                            }
                            AddHorizontalSpace(4)
                            Text(
                                detailsState.value.bodyPart?.measuringUnit ?: MeasuringUnit.CM.code
                            )
                            AddHorizontalSpace(5)
                            IconButton(onClick = {
                                isDetailBottomSheet = true
                            }) {
                                Icon(Icons.Rounded.ArrowDownward, contentDescription = "")
                            }
                        }
                    )
                    ChartTimeRangeButtons(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        selectedRangeButton = detailsState.value.timeRange,
                        onClick = {
                            onEvent(DetailsEvent.OnTimeRangeChange(it))
                        })

                    AddVerticalSpace(20)

                    LineGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(ratio = 2 / 1f)
                            .background(MaterialTheme.colorScheme.background)
                            .padding(16.dp), bodyPartValues = detailsState.value.chartBodyPartValue
                    )
                    HistorySection(
                        modifier = Modifier,
                        detailsState.value.allBodyPartValue,
                        detailsState.value.bodyPart?.measuringUnit,
                        onDeleteIconClick = {
                            onEvent(DetailsEvent.DeleteBodyPartValue(it))
                        }
                    )
                }

                NewValueInputBar(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    isInputCardVisible = isInputCardVisible,
                    date = detailsState.value.date.changeLocalDateToDateString(),
                    value = detailsState.value.textFieldValue,
                    onValueChange = { onEvent(DetailsEvent.OnTextFieldValueChange(it)) },
                    onDone = {
                        focusController.clearFocus()
                        onEvent(DetailsEvent.AddNewValue)
                    },
                    onImeAction = KeyboardActions {
                        focusController.clearFocus()
                    },
                    onCalenderClick = {
                        isDatePickerDialogOpen = true
                    }
                )

                InputCardHideIcon(
                    Modifier.align(Alignment.BottomEnd),
                    isInputValueCardVisible = isInputCardVisible,
                    onIconClick = {
                        isInputCardVisible = !isInputCardVisible
                    })
            }
        }

        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                MyTopBar(
                    screenName = AppScreenName.DetailsScreen,
                    title = detailsState.value.bodyPart?.name ?: "",
                    navController = navController,
                    action = {
                        IconButton(onClick = {
                            isDeleteItemDialogOpen = true
                        }) {
                            Icon(Icons.Rounded.Delete, contentDescription = "")
                        }
                        AddHorizontalSpace(4)
                        Text(detailsState.value.bodyPart?.measuringUnit ?: MeasuringUnit.CM.code)
                        AddHorizontalSpace(5)
                        IconButton(onClick = {
                            isDetailBottomSheet = true
                        }) {
                            Icon(Icons.Rounded.ArrowDownward, contentDescription = "")
                        }
                    }
                )
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        ChartTimeRangeButtons(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            selectedRangeButton = detailsState.value.timeRange,
                            onClick = {
                                onEvent(DetailsEvent.OnTimeRangeChange(it))
                            })

                        AddVerticalSpace(20)

                        LineGraph(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(ratio = 2 / 1f)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(16.dp),
                            bodyPartValues = detailsState.value.chartBodyPartValue
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                    ) {
                        HistorySection(
                            modifier = Modifier,
                            detailsState.value.allBodyPartValue,
                            detailsState.value.bodyPart?.measuringUnit,
                            onDeleteIconClick = {
                                onEvent(DetailsEvent.DeleteBodyPartValue(it))
                            }
                        )

                        NewValueInputBar(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            isInputCardVisible = isInputCardVisible,
                            date = dateState.selectedDateMillis.changeToDate()
                                .changeLocalDateToDateString(),
                            value = detailsState.value.textFieldValue,
                            onValueChange = { onEvent(DetailsEvent.OnTextFieldValueChange(it)) },
                            onDone = {
                                focusController.clearFocus()
                                onEvent(DetailsEvent.AddNewValue)
                            },
                            onImeAction = KeyboardActions {
                                focusController.clearFocus()
                            },
                            onCalenderClick = {
                                isDatePickerDialogOpen = true
                            }
                        )

                        InputCardHideIcon(
                            Modifier.align(Alignment.BottomEnd),
                            isInputValueCardVisible = isInputCardVisible,
                            onIconClick = {
                                isInputCardVisible = !isInputCardVisible
                            })
                    }
                }
            }

        }
    }


}

@Composable
private fun InputCardHideIcon(
    modifier: Modifier = Modifier,
    isInputValueCardVisible: Boolean,
    onIconClick: () -> Unit
) {

    IconButton(modifier = modifier, onClick = onIconClick) {
        Icon(
            if (isInputValueCardVisible) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistorySection(
    modifier: Modifier = Modifier, bodyPartValues: List<BodyPartValue>,
    measuringUnitCode: String?,
    onDeleteIconClick: (BodyPartValue) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {
        val grouped = bodyPartValues.groupBy { it.date.month }

        item {
            Text("History", textDecoration = TextDecoration.Underline)
            AddVerticalSpace(20)
        }
        grouped.forEach { (month, bodyPartValue) ->
            stickyHeader {
                Text(month.name, style = MaterialTheme.typography.bodySmall)
                AddVerticalSpace(5)
            }
            items(bodyPartValue) { item ->
                HistoryCard(
                    Modifier.padding(bottom = 8.dp),
                    bodyPartValue = item,
                    measuringUnitCode = measuringUnitCode ?: "",
                    onDeleteIconClick = onDeleteIconClick
                )
            }
        }
    }
}

@Composable
private fun HistoryCard(
    modifier: Modifier = Modifier,
    bodyPartValue: BodyPartValue,
    measuringUnitCode: String?,
    onDeleteIconClick: (BodyPartValue) -> Unit
) {

    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.DateRange,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 5.dp)
            )
            Text(
                bodyPartValue.date.changeLocalDateToDateString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.weight(1f))
            Text(
                "${bodyPartValue.value.roundToDecimal(4)} ${measuringUnitCode ?: ""}",
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = {
                onDeleteIconClick(bodyPartValue)
            }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
        }
    }

}

