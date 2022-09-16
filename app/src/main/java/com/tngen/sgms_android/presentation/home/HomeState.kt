package com.tngen.sgms_android.presentation.home

import com.tngen.sgms_android.data.entity.settings.SensorEntity
import com.tngen.sgms_android.model.home.HomeSensorModel

sealed class HomeState {
    object UnInitialized: HomeState()

    object Loading: HomeState()

    data class GetSensorListSuccess(
        val sensorList: List<HomeSensorModel>
    ): HomeState()

    object Error: HomeState()
}