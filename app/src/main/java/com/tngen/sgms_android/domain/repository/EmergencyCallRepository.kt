package com.tngen.sgms_android.domain.repository

import com.tngen.sgms_android.data.entity.settings.EmergencyCallEntity

interface EmergencyCallRepository {
    suspend fun getEmergencyCallList(): List<EmergencyCallEntity>

    suspend fun getLocalEmergencyCallList(): List<EmergencyCallEntity>

    suspend fun insertEmergencyCallItem(emergencyCallItem: EmergencyCallEntity): Long

    suspend fun insertEmergencyCallList(emergencyCallList: List<EmergencyCallEntity>)

    suspend fun updateEmergencyCallItem(emergencyCallItem: EmergencyCallEntity)

    suspend fun getEmergencyCallItem(itemId: Long): EmergencyCallEntity?

    suspend fun deleteAll()

    suspend fun deleteEmergencyCallItem(id: Long)
}