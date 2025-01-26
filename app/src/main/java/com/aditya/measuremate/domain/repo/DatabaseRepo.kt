package com.aditya.measuremate.domain.repo

import com.aditya.measuremate.domain.models.common.User
import com.aditya.measuremate.domain.models.dashboard.BodyPart
import com.aditya.measuremate.domain.models.dashboard.BodyPartValue
import kotlinx.coroutines.flow.Flow

interface DatabaseRepo {
    fun getSignedUser() : Flow<User?>
    fun getAllBodyParts() : Flow<List<BodyPart>>
    fun getBodyPart(bodyPartId : String) : Flow<BodyPart?>
    suspend fun addUser() : Flow<Result<Boolean>>
    suspend fun upsertBodyPart(bodyPart : BodyPart) : Result<Boolean>
    suspend fun deleteBodyPart(bodyPartId : String) : Result<Boolean>
    suspend fun upsertBodyPartValue(bodyPartValue: BodyPartValue) : Result<Boolean>
}