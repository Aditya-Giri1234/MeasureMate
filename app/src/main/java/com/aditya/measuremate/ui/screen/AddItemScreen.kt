package com.aditya.measuremate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.event.add_item.AddItemEvent
import com.aditya.measuremate.ui.event_state.add_item.AddItemState
import com.aditya.measuremate.ui.widgets.add_item.AddItemCard
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName
import com.example.udemycourseshoppingapp.ui.components.AddVerticalSpace
import com.example.udemycourseshoppingapp.ui.components.MyTopBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun AddItemScreen(
    navController: NavController,
    windowWidthSizeClass: WindowWidthSizeClass,
    snackBarState: SnackbarHostState,
    uiState: State<AddItemState>,
    uiEvent: Flow<UiEvent>,
    onEvent: (AddItemEvent) -> Unit,
) {
    var isAddItemDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        uiEvent.onEach {
            when (it) {
                is UiEvent.SnackBar -> {
                    snackBarState.showSnackbar(it.msg)
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
            }
        }.launchIn(this)
    }

    MeasureMetaDialog(
        isOpen = isAddItemDialogOpen,
        title = "Add / Update a new item",
        body = {
            OutlinedTextField(value = uiState.value.textFieldValue, onValueChange = {
                onEvent(AddItemEvent.OnTextFieldValueChange(it))
            })
        },
        confirmButtonText = "Save",
        onDialogDismiss = {
            isAddItemDialogOpen = false
            onEvent(AddItemEvent.OnAddItemDialogDismiss)
        },
        onConfirmButtonClick = {
            isAddItemDialogOpen = false
            onEvent(AddItemEvent.UpsertItem)
        }
    )

    Column(modifier = Modifier
        .fillMaxSize()) {
        MyTopBar(
            screenName = AppScreenName.AddItemScreen,
            navController = navController,
            title = "Add Item",
            action = {
                IconButton(
                    onClick = {
                        isAddItemDialogOpen = true
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "")
                }
            },

            )
        AddVerticalSpace(10)
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(uiState.value.bodyParts) { item ->
                AddItemCard(
                    name = item.name,
                    isChecked = item.isActive,
                    onCheckChange = {
                        onEvent(AddItemEvent.OnItemIsActiveChanged(item))
                    },
                    onClick = {
                        isAddItemDialogOpen = true
                        onEvent(AddItemEvent.OnItemClick(item))
                    })
            }
        }
    }
}