package com.tngen.sgms_android.data.entity.settings

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EmergencyCallEntity (
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userName: String,
    val userNum: String
        )