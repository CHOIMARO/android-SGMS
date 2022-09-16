package com.tngen.sgms_android.model.settings

import com.tngen.sgms_android.model.CellType
import com.tngen.sgms_android.model.Model
import java.util.*

data class SettingsSensorModel(
    override val id: Long,
    override val type: CellType = CellType.SENSOR_REGISTRATION_CELL,
    val sensorId: Long,
    val sensorLocation: String,
    val createdAt : Long
): Model(id, type) {

}