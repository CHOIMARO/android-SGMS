package com.tngen.sgms_android.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.data.entity.settings.SensorEntity
import com.tngen.sgms_android.domain.db.GetSensorListUseCase
import com.tngen.sgms_android.domain.serial.SendSerialUseCase
import com.tngen.sgms_android.model.home.HomeSensorModel
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.serial.enum.Command
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSensorListUseCase: GetSensorListUseCase,
    private val sendSerialUseCase: SendSerialUseCase,
) : BaseViewModel() {

    private var _homeStateLiveData = MutableLiveData<HomeState>(HomeState.UnInitialized)
    val homeStateLiveData: LiveData<HomeState> = _homeStateLiveData
    var sensorList : List<SensorEntity> = listOf()

    override fun fetchData(): Job = viewModelScope.launch {

        sensorList = getSensorListUseCase()
        _homeStateLiveData.postValue(HomeState.GetSensorListSuccess(
            sensorList.map {
                HomeSensorModel(
                    id = it.id,
                    sensorId = it.sensorId,
                    sensorLocation = it.sensorLocation,
                )
            }
        ))

    }

    fun checkDataSetChange() {
        var state = false
        CoroutineScope(Dispatchers.IO).launch {
            val changedSensorList = getSensorListUseCase()

            if (sensorList.size != changedSensorList.size) {
                fetchData()
            } else {
                for (i: Int in 0..sensorList.size - 1) {
                    if (sensorList[i].sensorId != changedSensorList[i].sensorId) {
                        fetchData()
                        break
                    }
                }
            }
        }
    }

    fun settingCalibration(serialEntity: SerialEntity) = viewModelScope.launch {
        sendSerialUseCase(Command.CALIBRATION_SETTING, 'C',
            serialEntity.sensorId.toString(),
            serialEntity.o2.toString(),
            serialEntity.co.toString(),
            serialEntity.lel.toString(),
            serialEntity.co2.toString(),
            serialEntity.h2s.toString(),)
    }
}