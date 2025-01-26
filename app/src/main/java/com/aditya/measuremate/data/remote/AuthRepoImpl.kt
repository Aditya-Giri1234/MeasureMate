package com.aditya.measuremate.data.remote

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.aditya.measuremate.domain.repo.AuthRepo
import com.example.udemycourseshoppingapp.common.utils.helper.AuthState
import com.example.udemycourseshoppingapp.common.utils.helper.Constants
import com.example.udemycourseshoppingapp.common.utils.helper.MyLogger
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    val auth: FirebaseAuth, val credentialManager: CredentialManager,
) : AuthRepo {

    override val authState: Flow<AuthState>
        get() = callbackFlow {
            val authCallback = FirebaseAuth.AuthStateListener { user ->

                val state = if (user.currentUser != null) {
                    MyLogger.d(msg = "Authorized")
                    AuthState.Authorized
                } else {
                    MyLogger.d(msg = "UnAuthorized")
                    AuthState.UnAuthorized
                }

                trySend(state)

            }

            auth.addAuthStateListener(authCallback)

            awaitClose {
                auth.removeAuthStateListener(authCallback)
            }
        }.distinctUntilChanged()

    override suspend fun signInAnonymously() = callbackFlow<Result<Boolean>> {

        try {
            auth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(Result.success(true))
                } else {
                    trySend(Result.failure(it.exception ?: Exception("Something went wrong !")))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(Result.failure(e))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun signIn(context: Context) = callbackFlow<Result<Boolean>> {

        try {
            val authCredentialResult = getGoogleAuthCredentials(context)
            if (authCredentialResult.isSuccess) {
                val authCredential = authCredentialResult.getOrNull()
                if (authCredential != null) {
                    val authResult = auth.signInWithCredential(authCredential).await()
                    val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
                    trySend(Result.success(isNewUser))
                } else {
                    trySend(Result.failure(IllegalArgumentException("Auth credential is null")))
                }
            } else {
                trySend(
                    Result.failure(
                        authCredentialResult.exceptionOrNull() ?: Exception("Unknown Error")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(Result.failure(e))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun anonymousUserSignInWithGoogle(context: Context) = callbackFlow<Result<Boolean>> {

        try {
            val authCredentialResult = getGoogleAuthCredentials(context)
            if (authCredentialResult.isSuccess) {
                val authCredential = authCredentialResult.getOrNull()
                if (authCredential != null) {
                    auth.currentUser?.linkWithCredential(authCredential)?.await()
                    trySend(Result.success(true))
                } else {
                    trySend(Result.failure(IllegalArgumentException("Auth credential is null")))
                }
            } else {
                trySend(
                    Result.failure(
                        authCredentialResult.exceptionOrNull() ?: Exception("Unknown Error")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(Result.failure(e))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun signOut() = callbackFlow<Result<Boolean>> {

        try {
            auth.signOut()
            trySend(Result.success(true))
        }catch ( e : Exception){
            e.printStackTrace()
            trySend(Result.failure(e))
        }

        awaitClose{
            close()
        }

    }

    private suspend fun getGoogleAuthCredentials(context: Context): Result<AuthCredential?> {
        return try {
            val nonce = UUID.randomUUID().toString()
            val signInWithGoogle: GetSignInWithGoogleOption =
                GetSignInWithGoogleOption.Builder(Constants.GOOGLE_CLIENT_ID).setNonce(nonce)
                    .build()
            val request =
                GetCredentialRequest.Builder().addCredentialOption(signInWithGoogle).build()
            val credential = credentialManager.getCredential(context, request).credential
            val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
            val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            MyLogger.i(msg = "credential :-> $credential , idToken :-> $googleIdToken , authCredential :-> $authCredential")
            Result.success(authCredential)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

}