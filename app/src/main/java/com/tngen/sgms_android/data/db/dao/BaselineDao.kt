package com.tngen.sgms_android.data.db.dao

import androidx.room.*
import com.tngen.sgms_android.data.entity.settings.BaselineEntity

@Dao
interface BaselineDao {

    @Query("SELECT * FROM BaselineEntity")
    suspend fun getAll(): List<BaselineEntity>

    @Query("SELECT * FROM BaselineEntity WHERE id=:id")
    suspend fun getById(id: Long): BaselineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(baselineEntity: BaselineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(baselineEntityList: List<BaselineEntity>)

    @Query("DELETE FROM BaselineEntity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM BaselineEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(baselineEntity: BaselineEntity)

}