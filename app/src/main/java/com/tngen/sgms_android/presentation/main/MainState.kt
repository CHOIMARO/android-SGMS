package com.tngen.sgms_android.presentation.main

import com.tngen.sgms_android.data.entity.serial.SerialBaselineSettingResponseEntity
import com.tngen.sgms_android.data.entity.serial.SerialCalibrationSettingResponseEntity
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.data.entity.settings.SensorEntity

sealed class MainState {
    object UnInitialized: MainState()

    object Loading: MainState()

    data class ReceivedSerialDataSuccess(
        val data: SerialEntity
    ): MainState()

    data class ReceivedSerialCalibrationSettingSuccess(
        val data: SerialCalibrationSettingResponseEntity
    ): MainState()

    data class ReceivedSerialBaselineSettingSuccess(
        val data: SerialBaselineSettingResponseEntity
    ): MainState()

    data class GetSensorListSuccess(
        val data: List<SensorEntity>
    ): MainState()

    data class InsertHistoryItemSuccess(
        val data: Unit
    ): MainState()

    object Error: MainState()

}