package com.tngen.sgms_android.widget.adapter.viewholder.history

import com.tngen.sgms_android.databinding.ViewholderHistoryListBinding
import com.tngen.sgms_android.model.history.HistoryModel
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.prodiver.ResourcesProvider
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener
import com.tngen.sgms_android.widget.adapter.viewholder.ModelViewHolder
import java.text.SimpleDateFormat

class HistoryHistoryViewHolder(
    private val binding: ViewholderHistoryListBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<HistoryModel>(binding, viewModel, resourcesProvider) {
    val simpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")

    override fun reset() = with(binding){

    }

    override fun bindData(model: HistoryModel) {
        super.bindData(model)
        with(binding) {
            sensorIdTextView.text = model.sensorId.toString()
            o2ValueTextView.text = model.o2.toString()
            co2ValueTextView.text = model.co2.toString()
            coValueTextView.text = model.co.toString()
            h2sValueTextView.text = model.h2s.toString()
            lelValueTextView.text = model.lel.toString()
            createdAtTextView.text = simpleDateFormat.format(model.createdAt)
        }
    }

    override fun bindViews(model: HistoryModel, adapterListener: AdapterListener) = with(binding) {
//        if (adapterListener is EmergencyCallItemClickListener) {
//            root.setOnLongClickListener {
//                adapterListener.onClickItem(model)
//                return@setOnLongClickListener(true)
//            }
//        }
    }
}