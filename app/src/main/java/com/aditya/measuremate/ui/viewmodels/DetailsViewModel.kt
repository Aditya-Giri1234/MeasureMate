package com.aditya.measuremate.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import com.aditya.measuremate.domain.repo.DatabaseRepo
import com.aditya.measuremate.ui.event.details.DetailsEvent
import com.aditya.measuremate.ui.event_state.details.DetailsState
import com.aditya.measuremate.ui.navigation.AppNavigationScreens
import com.example.common.extension.changeToDate
import com.example.common.extension.roundToDecimal
import com.example.udemycourseshoppingapp.common.utils.helper.TimeRange
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
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val databaseRepo: DatabaseRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val bodyPartId = savedStateHandle.toRoute<AppNavigationScreens.DetailsScreen>().bodyPartId

    private val _uiScreenState = MutableStateFlow(DetailsState())
    val uiScreenState = combine(
        _uiScreenState,
        databaseRepo.getBodyPart(bodyPartId) ,
        databaseRepo.getAllBodyPartValues(bodyPartId)
    ) { state, bodyPart , bodyPartValues ->
        val currentDate = LocalDate.now()
        val last7DaysValue = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(7))
        }
        val last30DaysValue = bodyPartValues.filter { bodyPartValue ->
            bodyPartValue.date.isAfter(currentDate.minusDays(30))

        }
        state.copy(
            bodyPart = bodyPart ,
            allBodyPartValue = bodyPartValues ,
            chartBodyPartValue = when(state.timeRange){
                TimeRange.LAST7DAYS -> {
                    last7DaysValue
                }
                TimeRange.LAST30DAYS ->{
                    last30DaysValue
                }
                TimeRange.ALL_TIME -> {
                    bodyPartValues
                }
            }
        )
    }.catch { e ->
        _uiEvent.send(UiEvent.SnackBar("Something went wrong. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DetailsState()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.AddNewValue -> {
                val state = uiScreenState.value
                val id = state.allBodyPartValue.find { it.date == state.date }?.bodyPartValueId
                val bodyPartValue = BodyPartValue(
                    value = state.textFieldValue.roundToDecimal(2),
                    date = state.date ,
                    bodyPartId = bodyPartId ,
                    bodyPartValueId = id
                )
                upsertBodyPartValue(bodyPartValue)
                _uiScreenState.update { it.copy(textFieldValue = "") }
            }

            is DetailsEvent.ChangeMeasuringUnit -> {
                val bodyPart = uiScreenState.value.bodyPart?.copy(
                    measuringUnit = event.measuringUnit.code
                )
                upsertItem(bodyPart)
            }

            DetailsEvent.DeleteBodyPart -> {
                deleteBodyPart()
            }

            is DetailsEvent.DeleteBodyPartValue -> {
                deleteBodyPartValue(event.bodyPartValue)
                _uiScreenState.update {
                    it.copy(
                        recentlyDeletedBodyPartValue = event.bodyPartValue
                    )
                }

            }

            is DetailsEvent.OnDateChange -> {
                _uiScreenState.update {
                    it.copy(
                        date = event.millis.changeToDate()
                    )
                }
            }

            is DetailsEvent.OnTextFieldValueChange -> {
                _uiScreenState.update {
                    it.copy(
                        textFieldValue = event.value
                    )
                }
            }

            is DetailsEvent.OnTimeRangeChange -> {
                _uiScreenState.update {
                    it.copy(
                        timeRange = event.timeRange
                    )
                }

            }

            DetailsEvent.RestoreBodyPartValue -> {
                upsertBodyPartValue(uiScreenState.value.recentlyDeletedBodyPartValue)
                _uiScreenState.update {
                    it.copy(
                        recentlyDeletedBodyPartValue = null
                    )
                }
            }
        }
    }

    private fun upsertItem(bodyPart: BodyPart?) {
        viewModelScope.launch {
            bodyPart ?: return@launch
            databaseRepo.upsertBodyPart(bodyPart).onSuccess {
                _uiEvent.send(UiEvent.SnackBar("Body Part saved successfully !"))
            }.onFailure { e ->
                _uiEvent.send(UiEvent.SnackBar("Couldn't saved. ${e.message}"))
            }
        }
    }

    private fun deleteBodyPart() {
        viewModelScope.launch {
            databaseRepo.deleteBodyPart(bodyPartId).onSuccess {
                _uiEvent.send(UiEvent.Navigate)
                _uiEvent.send(UiEvent.SnackBar("Body Part deleted successfully !"))
            }.onFailure { e ->
                _uiEvent.send(UiEvent.SnackBar("Couldn't delete. ${e.message}"))
            }
        }
    }

    private fun upsertBodyPartValue(bodyPartValue: BodyPartValue?) {
        viewModelScope.launch {
            bodyPartValue ?: return@launch
            databaseRepo.upsertBodyPartValue(bodyPartValue).onSuccess {
                _uiEvent.send(UiEvent.SnackBar("Body Part Value saved successfully !"))
            }.onFailure { e ->
                _uiEvent.send(UiEvent.SnackBar("Couldn't saved. ${e.message}"))
            }
        }
    }

    private fun deleteBodyPartValue(bodyPartValue: BodyPartValue?) {
        viewModelScope.launch {
            bodyPartValue ?: return@launch
            databaseRepo.deleteBodyPartValue(bodyPartValue).onSuccess {
                _uiEvent.send(UiEvent.SnackBar("Body Part Value delete successfully !" , actionLabel = "Undo"))
            }.onFailure { e ->
                _uiEvent.send(UiEvent.SnackBar("Couldn't deleted. ${e.message}"))
            }
        }
    }


}