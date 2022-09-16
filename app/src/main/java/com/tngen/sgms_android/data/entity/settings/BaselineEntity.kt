package com.tngen.sgms_android.data.entity.settings

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class BaselineEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val o2: Double = 0.0,
    val lel: Double = 0.0,
    val co: Long = 0,
    val co2: Long = 0,
    val h2s: Long = 0
)