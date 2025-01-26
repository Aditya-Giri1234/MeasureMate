package com.aditya.measuremate.domain.repo

import android.content.Context
import com.aditya.measuremate.common.models.ApiResponse
import com.example.udemycourseshoppingapp.common.utils.helper.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    val authState : Flow<AuthState>

    suspend fun signInAnonymously() : Flow<Result<Boolean>>

    suspend fun signIn(context : Context) : Flow<Result<Boolean>>
    suspend fun anonymousUserSignInWithGoogle(context : Context) : Flow<Result<Boolean>>

    suspend fun signOut() : Flow<Result<Boolean>>


}