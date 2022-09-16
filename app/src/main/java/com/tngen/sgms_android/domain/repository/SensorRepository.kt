package com.tngen.sgms_android.domain.repository

import com.tngen.sgms_android.data.entity.settings.SensorEntity

interface SensorRepository {

    suspend fun getSensorList(): List<SensorEntity>

    suspend fun getLocalSensorList(): List<SensorEntity>

    suspend fun insertSensorItem(sensorItem: SensorEntity): Long

    suspend fun insertSensorList(sensorList: List<SensorEntity>)

    suspend fun updateSensorItem(sensorItem: SensorEntity)

    suspend fun getSensorItem(itemId: Long): SensorEntity?

    suspend fun deleteAll()

    suspend fun deleteSensorItem(id: Long)

}
