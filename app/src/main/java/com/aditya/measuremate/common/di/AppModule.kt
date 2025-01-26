package com.aditya.measuremate.common.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.aditya.measuremate.data.remote.AuthRepoImpl
import com.aditya.measuremate.data.remote.DatabaseRepoImpl
import com.aditya.measuremate.domain.repo.AuthRepo
import com.aditya.measuremate.domain.repo.DatabaseRepo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getFirebaseAuth() : FirebaseAuth{
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore() : FirebaseFirestore{
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context : Context
    ) : CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    @Singleton
    fun getAuthRepo(auth : FirebaseAuth  , credentialManager: CredentialManager) : AuthRepo{
        return AuthRepoImpl(auth , credentialManager)
    }

    @Provides
    @Singleton
    fun getDatabase(auth : FirebaseAuth , firestore : FirebaseFirestore) :  DatabaseRepo{
        return DatabaseRepoImpl(auth , firestore)
    }
}