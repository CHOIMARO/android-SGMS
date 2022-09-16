package com.tngen.sgms_android.data.repository

import com.tngen.sgms_android.data.db.dao.BaselineDao
import com.tngen.sgms_android.data.db.dao.EmergencyCallDao
import com.tngen.sgms_android.data.entity.settings.EmergencyCallEntity
import com.tngen.sgms_android.domain.repository.EmergencyCallRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EmergencyCallRepositoryImpl
    @Inject constructor(
    private val emergencyCallDao: EmergencyCallDao,
    private val ioDispatcher: CoroutineDispatcher
) : EmergencyCallRepository {
    override suspend fun getEmergencyCallList(): List<EmergencyCallEntity> = withContext(ioDispatcher) {
        return@withContext emergencyCallDao.getAll()
    }

    override suspend fun getLocalEmergencyCallList(): List<EmergencyCallEntity> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun insertEmergencyCallItem(emergencyCallItem: EmergencyCallEntity): Long = withContext(ioDispatcher) {
        emergencyCallDao.insert(emergencyCallItem)
    }

    override suspend fun insertEmergencyCallList(emergencyCallList: List<EmergencyCallEntity>) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun updateEmergencyCallItem(emergencyCallItem: EmergencyCallEntity) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getEmergencyCallItem(itemId: Long): EmergencyCallEntity? = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteEmergencyCallItem(id: Long) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}