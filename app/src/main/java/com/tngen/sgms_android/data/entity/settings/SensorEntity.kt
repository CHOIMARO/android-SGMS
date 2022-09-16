package com.tngen.sgms_android.data.entity.settings

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class SensorEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val createdAt: Long,
    val sensorId: Long,
    val sensorLocation: String
)