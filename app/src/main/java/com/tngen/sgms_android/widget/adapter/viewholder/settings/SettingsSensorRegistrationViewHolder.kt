package com.tngen.sgms_android.widget.adapter.viewholder.settings

import com.tngen.sgms_android.databinding.ViewholderSettingsSensorRegistrationBinding
import com.tngen.sgms_android.model.settings.SettingsSensorModel
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.prodiver.ResourcesProvider
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener
import com.tngen.sgms_android.widget.adapter.listener.settings.SensorItemClickListener
import com.tngen.sgms_android.widget.adapter.viewholder.ModelViewHolder
import java.text.SimpleDateFormat
import java.util.*

class SettingsSensorRegistrationViewHolder(
    private val binding: ViewholderSettingsSensorRegistrationBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<SettingsSensorModel>(binding, viewModel, resourcesProvider) {
    override fun reset() = with(binding){

    }

    override fun bindData(model: SettingsSensorModel) {
        super.bindData(model)
        with(binding) {
            val date = Date(model.createdAt)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("ko","KR"))
            sensorCreatedAt.text =  dateFormat.format(date)
            sensorIdTextView.text = model.sensorId.toString()
            sensorLocationTextView.text = model.sensorLocation
        }
    }

    override fun bindViews(model: SettingsSensorModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is SensorItemClickListener) {
//            root.setOnClickListener {
//                adapterListener.onClickItem(model)
//            }
            root.setOnLongClickListener {
                adapterListener.onClickItem(model)
                return@setOnLongClickListener(true)
            }
        }
    }
}