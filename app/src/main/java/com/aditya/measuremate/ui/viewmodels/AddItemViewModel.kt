package com.aditya.measuremate.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.domain.repo.DatabaseRepo
import com.aditya.measuremate.ui.event.add_item.AddItemEvent
import com.aditya.measuremate.ui.event_state.add_item.AddItemState
import com.example.udemycourseshoppingapp.common.utils.helper.MeasuringUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val databaseRepo: DatabaseRepo
) : ViewModel() {

    private val _uiScreenState = MutableStateFlow(AddItemState())
    val uiScreenState = combine(
        _uiScreenState,
        databaseRepo.getAllBodyParts()
    ) { state, bodyParts ->
        state.copy(
            bodyParts = bodyParts
        )
    }.catch { e ->
        _uiEvent.send(UiEvent.SnackBar("Something went wrong. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = AddItemState()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent get() = _uiEvent.receiveAsFlow()


    fun onEvent(event: AddItemEvent) {
        when (event) {
            AddItemEvent.OnAddItemDialogDismiss -> {
                _uiScreenState.update {
                    it.copy(
                        textFieldValue = "" ,
                        selectedBodyPart = null
                    )
                }
            }

            is AddItemEvent.OnItemClick -> {
                _uiScreenState.update {
                    it.copy(
                        textFieldValue = event.bodyPart.name ,
                        selectedBodyPart = event.bodyPart
                    )
                }
            }

            is AddItemEvent.OnItemIsActiveChanged -> {
                val bodyPart = event.bodyPart
                upsertItem(
                    bodyPart.copy(
                        isActive = !bodyPart.isActive
                    )
                )
            }

            is AddItemEvent.OnTextFieldValueChange -> {
                _uiScreenState.update {
                    it.copy(
                        textFieldValue = event.value
                    )
                }
            }

            AddItemEvent.UpsertItem -> {
                val selectedBodyPart = uiScreenState.value.selectedBodyPart
                val bodyPart = BodyPart(
                    name = uiScreenState.value.textFieldValue.trim(),
                    isActive = selectedBodyPart?.isActive ?: true,
                    measuringUnit = selectedBodyPart?.measuringUnit ?: MeasuringUnit.CM.code ,
                    bodyPartId = selectedBodyPart?.bodyPartId
                )
                upsertItem(bodyPart)
                _uiScreenState.update {
                    it.copy(
                        textFieldValue = ""
                    )
                }
            }
        }
    }

    private fun upsertItem(bodyPart: BodyPart) {
        viewModelScope.launch {
            databaseRepo.upsertBodyPart(bodyPart).onSuccess {
                _uiEvent.send(UiEvent.SnackBar("Body Part saved successfully !"))
            }.onFailure { e ->
                _uiEvent.send(UiEvent.SnackBar("Couldn't saved. ${e.message}"))
            }
        }
    }
}