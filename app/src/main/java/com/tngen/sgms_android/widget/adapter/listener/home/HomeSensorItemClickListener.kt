package com.tngen.sgms_android.widget.adapter.listener.home

import com.tngen.sgms_android.model.home.HomeSensorModel
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener

interface HomeSensorItemClickListener: AdapterListener {
    fun onClickItem(model: HomeSensorModel)
}