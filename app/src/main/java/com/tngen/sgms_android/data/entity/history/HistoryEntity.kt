package com.tngen.sgms_android.data.entity.history

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val createdAt: Long,
    val sensorId: Long,
    val o2: Double,
    val co2: Double,
    val co: Double,
    val h2s: Double,
    val lel: Double
) {
}