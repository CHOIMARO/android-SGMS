package com.tngen.sgms_android.data.repository

import com.tngen.sgms_android.data.db.dao.HistoryDao
import com.tngen.sgms_android.data.entity.history.HistoryEntity
import com.tngen.sgms_android.di.CoroutinesQualifiers
import com.tngen.sgms_android.domain.repository.HistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepositoryImpl
    @Inject constructor(
    private val historyDao: HistoryDao,
    private val ioDispatcher : CoroutineDispatcher
): HistoryRepository {

    override suspend fun getHistoryList(): List<HistoryEntity> = withContext(ioDispatcher) {
        historyDao.getAll()
    }

    override suspend fun getLocalHistoryList(): List<HistoryEntity> = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun insertHistoryItem(historyItem: HistoryEntity): Long = withContext(ioDispatcher) {
        historyDao.insert(historyItem)
    }

    override suspend fun insertHistoryList(historyList: List<HistoryEntity>) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun updateHistoryItem(historyItem: HistoryEntity) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun getHistoryItem(itemId: Long): HistoryEntity? = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteHistoryItem(id: Long) = withContext(ioDispatcher) {
        TODO("Not yet implemented")
    }
}