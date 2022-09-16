package com.tngen.sgms_android.widget.adapter.listener.history

import com.tngen.sgms_android.model.history.HistoryModel
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener

interface HistoryItemClickListener : AdapterListener {
    fun onClickItem(model: HistoryModel)
}