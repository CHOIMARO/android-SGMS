package com.tngen.sgms_android.widget.adapter.viewholder.settings

import com.tngen.sgms_android.databinding.ViewholderSettingsEmergencyCallBinding
import com.tngen.sgms_android.model.settings.SettingsEmergencyCallModel
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.prodiver.ResourcesProvider
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener
import com.tngen.sgms_android.widget.adapter.listener.settings.EmergencyCallItemClickListener
import com.tngen.sgms_android.widget.adapter.viewholder.ModelViewHolder

class SettingsEmergencyCallViewHolder(
    private val binding: ViewholderSettingsEmergencyCallBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<SettingsEmergencyCallModel>(binding, viewModel, resourcesProvider) {
    override fun reset() = with(binding){

    }

    override fun bindData(model: SettingsEmergencyCallModel) {
        super.bindData(model)
        with(binding) {
            emergencyCallNameTextView.text = model.emergencyCallName
            emergencyCallNumTextView.text = model.emergencyCallNum
        }
    }

    override fun bindViews(model: SettingsEmergencyCallModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is EmergencyCallItemClickListener) {
            root.setOnLongClickListener {
                adapterListener.onClickItem(model)
                return@setOnLongClickListener(true)
            }
        }
    }
}