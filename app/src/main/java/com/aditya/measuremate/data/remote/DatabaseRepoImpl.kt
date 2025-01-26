package com.aditya.measuremate.data.remote

import com.aditya.measuremate.domain.models.common.User
import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import com.aditya.measuremate.domain.models.details.toBodyPartValueDto
import com.aditya.measuremate.domain.repo.DatabaseRepo
import com.example.udemycourseshoppingapp.common.utils.helper.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseRepoImpl @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val firestore: FirebaseFirestore
) : DatabaseRepo {

    private fun userCollection(): CollectionReference {
        return firestore.collection(Constants.USER_COLLECTION)
    }

    private fun bodyPartCollection(
        userId: String = firebaseAuth.currentUser?.uid.orEmpty()
    ): CollectionReference {
        return userCollection().document(userId).collection(Constants.BODY_PART_COLLECTION)
    }

    private fun bodyPartValueCollection(
        bodyPartId: String
    ): CollectionReference {
        return bodyPartCollection().document(bodyPartId)
            .collection(Constants.BODY_PART_VALUE_COLLECTION)
    }

    override fun getSignedUser(): Flow<User?> {
        return flow {
            try {
                val uid = firebaseAuth.currentUser?.uid.orEmpty()
                userCollection().document(uid).snapshots().collect { snapshot ->
                    emit(snapshot.toObject(User::class.java))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }

    override fun getAllBodyParts(): Flow<List<BodyPart>> {
        return flow {
            try {
                bodyPartCollection().orderBy(Constants.BODY_PART_NAME_FIELD).snapshots()
                    .collect { snapshot ->
                        emit(snapshot.toObjects<BodyPart>())
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override fun getBodyPart(bodyPartId: String): Flow<BodyPart?> {
        return flow {
            try {
                bodyPartCollection().document(bodyPartId).snapshots()
                    .collect { snapshot ->
                        emit(snapshot.toObject<BodyPart>())
                    }
            } catch (e: Exception) {
                throw e
            }
        }
    }


    override suspend fun addUser() = callbackFlow<Result<Boolean>> {

        try {
            val firebaseUser = firebaseAuth.currentUser
                ?: throw IllegalArgumentException("No Current User Logged In.")
            var user = User(
                userId = firebaseUser.uid,
                name = "Anonymouse",
                email = "anonymouse@measuremate.gmail",
                profilePictureUrl = "",
                isAnonymous = firebaseUser.isAnonymous
            )
            firebaseUser.providerData.forEach { profile ->
                user = user.copy(
                    name = profile.displayName ?: "Anonymouse",
                    email = profile.email ?: "anonymouse@measuremate.gmail",
                    profilePictureUrl = profile.photoUrl?.toString() ?: ""
                )
            }
            userCollection().document(firebaseUser.uid).set(user).await()
            trySend(Result.success(true))
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(Result.failure(e))
        }

        awaitClose {
            close()
        }
    }

    override suspend fun upsertBodyPart(bodyPart: BodyPart): Result<Boolean> {
        return try {
            val bodyPartId = bodyPart.bodyPartId ?: bodyPartCollection().document().id
            val updatedBodyPart = bodyPart.copy(
                bodyPartId = bodyPartId
            )
            bodyPartCollection().document(bodyPartId).set(updatedBodyPart).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteBodyPart(bodyPartId: String): Result<Boolean> {
        return try {
            bodyPartCollection().document(bodyPartId).delete().await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun upsertBodyPartValue(bodyPartValue: BodyPartValue): Result<Boolean> {
        return try {
            val bodyPartValueCollection = bodyPartValueCollection(bodyPartId = bodyPartValue.bodyPartId.orEmpty())
            val documentId = bodyPartValue.bodyPartValueId ?: bodyPartValueCollection.document().id
            val bodyPartValueDto = bodyPartValue.toBodyPartValueDto().copy(
                bodyPartValueId = documentId
            )
            bodyPartValueCollection.document(documentId).set(bodyPartValueDto).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}