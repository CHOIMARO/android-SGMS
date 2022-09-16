package com.tngen.sgms_android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tngen.sgms_android.data.db.dao.BaselineDao
import com.tngen.sgms_android.data.db.dao.EmergencyCallDao
import com.tngen.sgms_android.data.db.dao.HistoryDao
import com.tngen.sgms_android.data.db.dao.SensorDao
import com.tngen.sgms_android.data.entity.history.HistoryEntity
import com.tngen.sgms_android.data.entity.settings.BaselineEntity
import com.tngen.sgms_android.data.entity.settings.EmergencyCallEntity
import com.tngen.sgms_android.data.entity.settings.SensorEntity
import com.tngen.sgms_android.utility.DateConverter

@Database(
    entities = [SensorEntity::class, BaselineEntity::class, EmergencyCallEntity::class, HistoryEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateConverter::class)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun sensorDao(): SensorDao

    abstract fun baselineDao(): BaselineDao

    abstract fun emergencyCallDao(): EmergencyCallDao

    abstract fun historyDao(): HistoryDao

    companion object {
        const val DB_NAME = "ApplicationDataBase.db"
    }

}
