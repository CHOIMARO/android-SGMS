package com.tngen.sgms_android.domain.repository

import com.tngen.sgms_android.data.entity.history.HistoryEntity

interface HistoryRepository {
    suspend fun getHistoryList(): List<HistoryEntity>

    suspend fun getLocalHistoryList(): List<HistoryEntity>

    suspend fun insertHistoryItem(historyItem: HistoryEntity): Long

    suspend fun insertHistoryList(historyList: List<HistoryEntity>)

    suspend fun updateHistoryItem(historyItem: HistoryEntity)

    suspend fun getHistoryItem(itemId: Long): HistoryEntity?

    suspend fun deleteAll()

    suspend fun deleteHistoryItem(id: Long)
}