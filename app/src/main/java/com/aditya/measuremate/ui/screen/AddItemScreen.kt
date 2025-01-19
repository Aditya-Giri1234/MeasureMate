package com.aditya.measuremate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aditya.measuremate.domain.models.dashboard.predefinedBodyParts
import com.aditya.measuremate.ui.compoenents.MeasureMetaDialog
import com.aditya.measuremate.ui.widgets.add_item.AddItemCard
import com.example.udemycourseshoppingapp.common.utils.helper.AppScreenName
import com.example.udemycourseshoppingapp.ui.components.MyTopBar

@Composable
fun AddItemScreen(navController: NavController, windowWidthSizeClass: WindowWidthSizeClass) {
    var isAddItemDialogOpen by rememberSaveable {
        mutableStateOf(false)
    }

    MeasureMetaDialog(
        isOpen = isAddItemDialogOpen,
        title = "Add a new item",
        body = {
            OutlinedTextField(value = "", onValueChange = {})
        },
        confirmButtonText = "Save",
        onDialogDismiss = {
            isAddItemDialogOpen = false
        },
        onConfirmButtonClick = {
            isAddItemDialogOpen = false
        }
    )

    Scaffold(
        topBar = {
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
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Adaptive(300.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(predefinedBodyParts) { item ->
                AddItemCard(
                    name = item.name,
                    isChecked = item.isActive,
                    onCheckChange = {},
                    onClick = {})
            }
        }
    }
}