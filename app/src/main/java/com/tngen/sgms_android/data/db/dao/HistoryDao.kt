package com.tngen.sgms_android.data.db.dao

import androidx.room.*
import com.tngen.sgms_android.data.entity.history.HistoryEntity

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HistoryEntity")
    suspend fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE id=:id")
    suspend fun getById(id: Long): HistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntity: HistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(historyEntityList: List<HistoryEntity>)

    @Query("DELETE FROM HistoryEntity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM HistoryEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(historyEntity: HistoryEntity)
}