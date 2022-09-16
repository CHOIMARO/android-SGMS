package com.tngen.sgms_android.widget.adapter.listener.settings

import com.tngen.sgms_android.model.settings.SettingsSensorModel
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener

interface SensorItemClickListener: AdapterListener {
    fun onClickItem(model: SettingsSensorModel)
}