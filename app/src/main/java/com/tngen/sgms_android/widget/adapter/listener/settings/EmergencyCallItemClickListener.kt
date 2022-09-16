package com.tngen.sgms_android.widget.adapter.listener.settings

import com.tngen.sgms_android.model.settings.SettingsEmergencyCallModel
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener

interface EmergencyCallItemClickListener: AdapterListener {
    fun onClickItem(model: SettingsEmergencyCallModel)
}