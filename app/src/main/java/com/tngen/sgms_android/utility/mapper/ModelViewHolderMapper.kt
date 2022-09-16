package com.tngen.sgms_android.utility.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.databinding.ViewholderHistoryListBinding
import com.tngen.sgms_android.databinding.ViewholderSettingsEmergencyCallBinding
import com.tngen.sgms_android.databinding.ViewholderHomeSensorListBinding
import com.tngen.sgms_android.databinding.ViewholderSettingsSensorRegistrationBinding
import com.tngen.sgms_android.model.CellType
import com.tngen.sgms_android.model.Model
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.prodiver.ResourcesProvider
import com.tngen.sgms_android.widget.adapter.viewholder.ModelViewHolder
import com.tngen.sgms_android.widget.adapter.viewholder.history.HistoryHistoryViewHolder
import com.tngen.sgms_android.widget.adapter.viewholder.home.HomeSensorListViewHolder
import com.tngen.sgms_android.widget.adapter.viewholder.settings.SettingsEmergencyCallViewHolder
import com.tngen.sgms_android.widget.adapter.viewholder.settings.SettingsSensorRegistrationViewHolder

object ModelViewHolderMapper {

    @Suppress("UNCHECKED_CAST")
    fun <M: Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider,
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.SENSOR_REGISTRATION_CELL ->
                SettingsSensorRegistrationViewHolder(
                    ViewholderSettingsSensorRegistrationBinding.inflate(inflater, parent, false),
                    viewModel,
                    resourcesProvider
                )
            CellType.EMERGENCY_CALL_CELL ->
                SettingsEmergencyCallViewHolder(
                    ViewholderSettingsEmergencyCallBinding.inflate(inflater, parent, false),
                    viewModel,
                    resourcesProvider
                )
            CellType.HOME_SENSOR_LIST_CELL ->
                HomeSensorListViewHolder(
                    ViewholderHomeSensorListBinding.inflate(inflater, parent, false),
                    viewModel,
                    resourcesProvider,
                )
            CellType.HISTORY_HISTORY_LIST_CELL ->
                HistoryHistoryViewHolder(
                    ViewholderHistoryListBinding.inflate(inflater, parent, false),
                    viewModel,
                    resourcesProvider
                )
        }

        return viewHolder as ModelViewHolder<M>
    }
}