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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.domain.models.dashboard.predefinedBodyParts
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.event.add_item.AddItemEvent
import com.aditya.measuremate.ui.viewmodels.AddItemViewModel
import com.aditya.measuremate.ui.widgets.add_item.AddItemCard
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName
import com.example.udemycourseshoppingapp.ui.components.AddVerticalSpace
import com.example.udemycourseshoppingapp.ui.components.MyTopBar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun AddItemScreen(navController: NavController, windowWidthSizeClass: WindowWidthSizeClass , innerPadding: PaddingValues , snackBarState : SnackbarHostState  , addItemViewModel: AddItemViewModel) {
    var isAddItemDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val uiState = addItemViewModel.uiScreenState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        addItemViewModel.uiEvent.onEach {
            when (it) {
                is UiEvent.SnackBar -> {
                    snackBarState.showSnackbar(it.msg)
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}            }
        }.launchIn(this)
    }

    MeasureMetaDialog(
        isOpen = isAddItemDialogOpen,
        title = "Add / Update a new item",
        body = {
            OutlinedTextField(value = uiState.value.textFieldValue, onValueChange = {
                addItemViewModel.onEvent(AddItemEvent.OnTextFieldValueChange(it))
            })
        },
        confirmButtonText = "Save",
        onDialogDismiss = {
            isAddItemDialogOpen = false
            addItemViewModel.onEvent(AddItemEvent.OnAddItemDialogDismiss)
        },
        onConfirmButtonClick = {
            isAddItemDialogOpen = false
            addItemViewModel.onEvent(AddItemEvent.UpsertItem)
        }
    )

        Column (modifier = Modifier.padding(innerPadding).fillMaxSize()){
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
                        Icon(Icons.Filled.Add , contentDescription = "")
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
                            addItemViewModel.onEvent(AddItemEvent.OnItemIsActiveChanged(item))
                        },
                        onClick = {
                            addItemViewModel.onEvent(AddItemEvent.OnItemClick(item))
                        })
                }
            }
        }
}