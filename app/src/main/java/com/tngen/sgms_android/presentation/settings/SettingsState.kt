package com.tngen.sgms_android.presentation.settings

import com.tngen.sgms_android.data.entity.settings.BaselineEntity
import com.tngen.sgms_android.model.settings.SettingsEmergencyCallModel
import com.tngen.sgms_android.model.settings.SettingsSensorModel

sealed class SettingsState {
    object UnInitialized: SettingsState()

    object Loading: SettingsState()

    data class GetSensorSuccess(
        val sensorList: List<SettingsSensorModel>
    ): SettingsState()

    data class GetBaselineSuccess(
        val baseline: BaselineEntity?
    ): SettingsState()

    data class GetEmergencyCallSuccess(
        val emergencyCallList: List<SettingsEmergencyCallModel>
    ): SettingsState()

    object Error: SettingsState()

}