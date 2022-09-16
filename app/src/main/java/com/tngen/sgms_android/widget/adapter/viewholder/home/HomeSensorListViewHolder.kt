package com.tngen.sgms_android.widget.adapter.viewholder.home

import com.tngen.sgms_android.databinding.ViewholderHomeSensorListBinding
import com.tngen.sgms_android.model.home.HomeSensorModel
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.prodiver.ResourcesProvider
import com.tngen.sgms_android.widget.adapter.listener.AdapterListener
import com.tngen.sgms_android.widget.adapter.listener.home.HomeSensorItemClickListener
import com.tngen.sgms_android.widget.adapter.viewholder.ModelViewHolder
import java.util.*

class HomeSensorListViewHolder(
    private val binding: ViewholderHomeSensorListBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider,
) : ModelViewHolder<HomeSensorModel>(binding, viewModel, resourcesProvider) {
    override fun reset() = with(binding){

    }

    override fun bindData(model: HomeSensorModel) {
        super.bindData(model)
        with(binding) {
            sensorIdTextView.text = model.sensorId.toString()
            sensorLocationTextView.text = model.sensorLocation
            o2ValueTextView.text = model.o2.toString()
            co2ValueTextView.text = model.co2.toString()
            coValueTextView.text = model.co.toString()
            h2sValueTextView.text = model.h2s.toString()
            lelValueTextView.text = model.lel.toString()

            initClickBar()
            initArrowImageButton()

        }
    }
    private fun initClickBar() = with(binding) {
        expandableLayout.duration = 300
        sensorLinearLayout.setOnClickListener {
            if (expandableLayout.isExpanded) {
                arrowImageButton.animate().rotation(0f).duration = 300
                expandableLayout.collapse()
            } else {
                expandableLayout.expand()
                arrowImageButton.animate().rotation(180f).duration = 300
            }
        }
    }
    private fun initArrowImageButton() = with(binding) {
        arrowImageButton.setOnClickListener {
            if (expandableLayout.isExpanded) {
                arrowImageButton.animate().rotation(0f).duration = 300
                expandableLayout.collapse()
            } else {
                expandableLayout.expand()
                arrowImageButton.animate().rotation(180f).duration = 300
            }
        }
    }

    override fun bindViews(model: HomeSensorModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is HomeSensorItemClickListener) {
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }
}