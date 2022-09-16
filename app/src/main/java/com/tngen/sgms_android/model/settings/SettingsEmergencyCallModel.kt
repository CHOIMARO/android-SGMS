package com.tngen.sgms_android.model.settings

import com.tngen.sgms_android.model.CellType
import com.tngen.sgms_android.model.Model

data class SettingsEmergencyCallModel(
    override val id: Long,
    override val type: CellType = CellType.EMERGENCY_CALL_CELL,
    val emergencyCallName: String,
    val emergencyCallNum: String,

) : Model(id, type)


