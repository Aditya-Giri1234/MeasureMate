package com.aditya.measuremate.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.domain.repo.AuthRepo
import com.aditya.measuremate.domain.repo.DatabaseRepo
import com.aditya.measuremate.ui.event.dashboard.DashboardEvent
import com.aditya.measuremate.ui.event_state.dashboard.DashboardState
import com.example.udemycourseshoppingapp.common.utils.helper.AuthState
import com.example.udemycourseshoppingapp.common.utils.helper.MyLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val databaseRepo: DatabaseRepo
) : ViewModel() {

    private val _dashBoardScreenState = MutableStateFlow(DashboardState())
    val dashBoardScreenState = combine(
        _dashBoardScreenState,
        databaseRepo.getSignedUser(),
        databaseRepo.getAllBodyParts()
    ) { state, user, bodyParts ->
        val activeBodyPart = bodyParts.filter { it.isActive }
        state.copy(
            user = user,
            bodyPart = activeBodyPart
        )
    }.catch { e ->
        _uiEvent.send(UiEvent.SnackBar("Something went wrong. ${e.message}"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    private val _authState = MutableStateFlow(AuthState.Loading)
    val authState get() = _authState.asStateFlow()

    init {
        subscribeToAuthState()
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.AnonymouslyUserSignInGoogle -> {
                anonymousUserSignInWithGoogle(event.context)
            }

            DashboardEvent.SignOut -> {
                signOut()
            }
        }
    }

    private fun anonymousUserSignInWithGoogle(context: Context) {
        viewModelScope.launch {
            _dashBoardScreenState.update {
                it.copy(isSignInButtonLoading = true)
            }
            authRepo.anonymousUserSignInWithGoogle(context).first().onSuccess {
                databaseRepo.addUser().first().onSuccess {
                    _uiEvent.send(UiEvent.HideBottomSheet)
                    _uiEvent.send(UiEvent.SnackBar("Sign In Successfully !"))
                }.onFailure { e ->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't add user. ${e.message}"))
                }
            }.onFailure {
                _uiEvent.send(UiEvent.SnackBar("Something went wrong. Please try later !"))
            }


            _dashBoardScreenState.update {
                it.copy(
                    isSignInButtonLoading = false
                )
            }
        }
    }


    private fun signOut() {
        viewModelScope.launch {

            _dashBoardScreenState.update {
                it.copy(
                    isSignOutButtonLoading =  true
                )
            }
            val response = authRepo.signOut().first()

            if (response.isSuccess) {
                _uiEvent.send(UiEvent.HideBottomSheet)
                _uiEvent.send(UiEvent.SnackBar("Signed out successfully !"))
            } else {
                _uiEvent.send(UiEvent.SnackBar("Couldn't signed out. ${response.exceptionOrNull() ?: ""}"))
            }

            _dashBoardScreenState.update {
                it.copy(
                    isSignOutButtonLoading =  false
                )
            }

        }
    }


    private fun subscribeToAuthState() = viewModelScope.launch {
        authRepo.authState.onEach {
            MyLogger.d(msg = "Emit value : - > ${it.name}")
            _authState.tryEmit(it)
        }.launchIn(this)
    }
}