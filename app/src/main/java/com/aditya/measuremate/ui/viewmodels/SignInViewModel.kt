package com.aditya.measuremate.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aditya.measuremate.common.manager.SoftwareManager
import com.aditya.measuremate.common.utils.UiEvent
import com.aditya.measuremate.data.remote.AuthRepoImpl
import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.domain.models.dashboard.predefinedBodyParts
import com.aditya.measuremate.domain.repo.AuthRepo
import com.aditya.measuremate.domain.repo.DatabaseRepo
import com.aditya.measuremate.ui.event.sign.SignInEvent
import com.aditya.measuremate.ui.event_state.sign.SignInState
import com.example.udemycourseshoppingapp.common.utils.helper.AuthState
import com.example.udemycourseshoppingapp.common.utils.helper.MyLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val authRepo: AuthRepo,
    val databaseRepo: DatabaseRepo,
    val app: Application
) : AndroidViewModel(app) {
    private val _signInState = MutableStateFlow(SignInState())
    val signInState get() = _signInState.asStateFlow()


    private val _uiEvent = Channel<UiEvent>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    private val _authState = MutableStateFlow(AuthState.Loading)
    val authState get() = _authState.asStateFlow()

    init {
        subscribeToAuthState()
    }

    fun onEvent(event: SignInEvent) {
        when (event) {
            SignInEvent.SignInAnonymously -> {
                signInAnonymously()
            }

            is SignInEvent.SignInWithGoogle -> {
                signInWithGoogle(event.context)
            }
        }
    }

    private fun signInAnonymously() = viewModelScope.launch(Dispatchers.IO) {
        _signInState.update {
            it.copy(
                isAnonymousSignInButtonLoading = true
            )
        }

        val isSuccess = authRepo.signInAnonymously().first().onSuccess { isNewUser ->
            if (isNewUser) {
                databaseRepo.addUser().first().onSuccess {
                    try{
                        insertPredefinedBodyPartValue()
                        _uiEvent.send(UiEvent.SnackBar("Sign In Successfully !"))
                    }catch(e : Exception){
                        _uiEvent.send(UiEvent.SnackBar("Signed in,but failed to insert predefined body parts. ${e.message}"))
                    }
                }.onFailure { e->
                    _uiEvent.send(UiEvent.SnackBar("Couldn't add user. ${e.message}"))
                }
            } else {
                _uiEvent.send(UiEvent.SnackBar("Sign In Successfully !"))
            }


        }.onFailure {
            _uiEvent.send(UiEvent.SnackBar("Something went wrong. Please try later !"))
        }

        _signInState.update {
            it.copy(
                isAnonymousSignInButtonLoading = false
            )
        }
    }

    private fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            _signInState.update {
                it.copy(isGoogleSignInButtonLoading = true)
            }
            authRepo.signIn(context).first().onSuccess { isNewUser ->
                if (isNewUser) {
                    databaseRepo.addUser().first().onSuccess {
                        try{
                            insertPredefinedBodyPartValue()
                            _uiEvent.send(UiEvent.SnackBar("Sign In Successfully !"))
                        }catch(e : Exception){
                            _uiEvent.send(UiEvent.SnackBar("Signed in,but failed to insert predefined body parts. ${e.message}"))
                        }
                    }.onFailure { e->
                        _uiEvent.send(UiEvent.SnackBar("Couldn't add user. ${e.message}"))
                    }
                } else {
                    _uiEvent.send(UiEvent.SnackBar("Sign In Successfully !"))
                }

            }.onFailure {
                _uiEvent.send(UiEvent.SnackBar("Something went wrong. Please try later !"))
            }


            _signInState.update {
                it.copy(
                    isGoogleSignInButtonLoading = false
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


    private suspend fun insertPredefinedBodyPartValue(){
        predefinedBodyParts.forEach { bodyPart ->
            databaseRepo.upsertBodyPart(bodyPart)
        }
    }

}