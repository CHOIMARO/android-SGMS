package com.tngen.sgms_android.data.entity.serial

data class SerialCalibrationEntity (
        val sensorId: Long = 0,
        val o2: Double = 0.0,
        val co2: Double = 0.0,
        val co: Double = 0.0,
        val h2s: Double = 0.0,
        val lel: Double = 0.0
)
