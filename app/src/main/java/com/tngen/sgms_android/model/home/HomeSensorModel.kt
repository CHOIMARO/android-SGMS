package com.tngen.sgms_android.model.home

import com.github.mikephil.charting.data.Entry
import com.tngen.sgms_android.model.CellType
import com.tngen.sgms_android.model.Model

data class HomeSensorModel(
    override val id: Long,
    override val type: CellType = CellType.HOME_SENSOR_LIST_CELL,
    val sensorId: Long,
    val sensorLocation: String,
    var o2: Double? = null,
    var co2: Double? = null,
    var co: Double? = null,
    var h2s: Double? = null,
    var lel: Double? = null,
    var o2Entries: ArrayList<Entry> = arrayListOf(),
    var co2Entries: ArrayList<Entry> = arrayListOf(),
    var coEntries: ArrayList<Entry> = arrayListOf(),
    var h2sEntries: ArrayList<Entry> = arrayListOf(),
    var lelEntries: ArrayList<Entry> = arrayListOf(),
    var createdAt : Long? = null


    ) : Model(id, type)