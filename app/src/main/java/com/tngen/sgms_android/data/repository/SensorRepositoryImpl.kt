package com.tngen.sgms_android.data.repository

import com.tngen.sgms_android.data.db.dao.SensorDao
import com.tngen.sgms_android.data.entity.settings.SensorEntity
import com.tngen.sgms_android.domain.repository.SensorRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SensorRepositoryImpl
    @Inject constructor(
    private val sensorDao: SensorDao,
    private val ioDispatcher: CoroutineDispatcher
): SensorRepository {

    override suspend fun getSensorList(): List<SensorEntity> = withContext(ioDispatcher) {
        return@withContext sensorDao.getAll()
    }

    override suspend fun getLocalSensorList(): List<SensorEntity> = withContext(ioDispatcher) {
        sensorDao.getAll()
    }

    override suspend fun insertSensorItem(sensorItem: SensorEntity): Long = withContext(ioDispatcher) {
        sensorDao.insert(sensorItem)
    }

    override suspend fun insertSensorList(sensorList: List<SensorEntity>) {

    }

    override suspend fun updateSensorItem(sensorItem: SensorEntity) {

    }

    override suspend fun getSensorItem(itemId: Long): SensorEntity? = withContext(ioDispatcher) {
        sensorDao.getById(itemId)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        sensorDao.deleteAll()
    }

    override suspend fun deleteSensorItem(id: Long) {
        sensorDao.delete(id)
    }


}
