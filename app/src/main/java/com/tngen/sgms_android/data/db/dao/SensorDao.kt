package com.tngen.sgms_android.data.db.dao

import androidx.room.*
import com.tngen.sgms_android.data.entity.settings.SensorEntity

@Dao
interface SensorDao {

    @Query("SELECT * FROM SensorEntity")
    suspend fun getAll(): List<SensorEntity>

    @Query("SELECT * FROM SensorEntity WHERE id=:id")
    suspend fun getById(id: Long): SensorEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sensorEntity: SensorEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sensorEntityList: List<SensorEntity>)

    @Query("DELETE FROM SensorEntity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM SensorEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(sensorEntity: SensorEntity)

}
