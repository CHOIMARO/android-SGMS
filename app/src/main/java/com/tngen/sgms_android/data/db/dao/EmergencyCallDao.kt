package com.tngen.sgms_android.data.db.dao

import androidx.room.*
import com.tngen.sgms_android.data.entity.settings.EmergencyCallEntity

@Dao
interface EmergencyCallDao {
    @Query("SELECT * FROM EmergencyCallEntity")
    suspend fun getAll(): List<EmergencyCallEntity>

    @Query("SELECT * FROM EmergencyCallEntity WHERE id=:id")
    suspend fun getById(id: Long): EmergencyCallEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emergencyCallEntity: EmergencyCallEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emergencyCallEntityList: List<EmergencyCallEntity>)

    @Query("DELETE FROM EmergencyCallEntity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM EmergencyCallEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(emergencyCallEntity: EmergencyCallEntity)
}