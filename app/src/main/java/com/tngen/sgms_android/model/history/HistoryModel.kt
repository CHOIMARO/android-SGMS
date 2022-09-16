package com.tngen.sgms_android.model.history

import com.tngen.sgms_android.model.CellType
import com.tngen.sgms_android.model.Model

class HistoryModel(
    override val id: Long,
    override val type: CellType = CellType.HISTORY_HISTORY_LIST_CELL,
    val sensorId: Long,
    val sensorLocation: String = "",
    val createdAt : Long,
    var o2: Double,
    var co2: Double,
    var co: Double,
    var h2s: Double,
    var lel: Double,
): Model(id, type) {
}