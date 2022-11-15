package com.tngen.sgms_android.presentation.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.data.entity.settings.BaselineEntity
import com.tngen.sgms_android.data.entity.settings.SensorEntity
import com.tngen.sgms_android.domain.db.*
import com.tngen.sgms_android.domain.serial.SendSerialUseCase
import com.tngen.sgms_android.model.settings.SettingsEmergencyCallModel
import com.tngen.sgms_android.model.settings.SettingsSensorModel
import com.tngen.sgms_android.preferences.Preferences
import com.tngen.sgms_android.presentation.base.BaseViewModel
import com.tngen.sgms_android.utility.serial.enum.Command
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSensorListUseCase: GetSensorListUseCase,
    private val insertSensorItemUseCase: InsertSensorItemUseCase,
    private val deleteSensorItemUseCase: DeleteSensorItemUseCase,
    private val insertBaselineItemUsecase: InsertBaselineItemUsecase,
    private val getBaselineItemUseCase: GetBaselineItemUseCase,
    private val insertEmergencyCallItemUseCase: InsertEmergencyCallItemUseCase,
    private val deleteEmergecnyCallItemUseCase: DeleteEmergecnyCallItemUseCase,
    private val getEmergencyCallListUseCase: GetEmergencyCallListUseCase,
    private val sendSerialUseCase: SendSerialUseCase,

) : BaseViewModel() {

    private var sensorList = listOf<SensorEntity>()

    private var _settingSharedFlow = MutableSharedFlow<SettingsState>()
    val settingSharedFlow : SharedFlow<SettingsState> = _settingSharedFlow

    override fun fetchData(): Job = viewModelScope.launch {
        _settingSharedFlow.emit(SettingsState.Loading)

        sensorList = getSensorListUseCase()
        _settingSharedFlow.emit(SettingsState.GetSensorSuccess(sensorList.map {
            SettingsSensorModel (
                id = it.id,
                createdAt = it.createdAt,
                sensorId = it.sensorId,
                sensorLocation = it.sensorLocation
            )
        }))

        _settingSharedFlow.emit(SettingsState.GetBaselineSuccess(getBaselineItemUseCase(0)))
        _settingSharedFlow.emit(SettingsState.GetEmergencyCallSuccess(getEmergencyCallListUseCase().map {
            SettingsEmergencyCallModel(
                emergencyCallName = it.userName,
                emergencyCallNum = it.userNum,
                id = it.id
            )
        }))


    }
    fun registrationSensor(sensorId: Long, sensorLocation: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val deferredInt = async {
                insertSensorItemUseCase(sensorId, sensorLocation)
                1
            }
            val value = deferredInt.await()

            if(value == 1) {
                Log.d("InsertSensorItemUseCase", "데이터 저장 완료")
                fetchData()
            }
        }

    }

    fun checkDuplicateId(sensorId : Long) : Boolean {
        var checkDuplicateId = true

        run {
            sensorList.forEach {
                if(it.sensorId == sensorId) {
                    checkDuplicateId = false
                    return@run
                }else{
                    checkDuplicateId = true
                }
            }
        }
        return checkDuplicateId
    }

    fun removeSensorItem(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            val deferredInt = async {
                deleteSensorItemUseCase(id)
                1
            }
            val value = deferredInt.await()

            if(value == 1) {
                Log.d("InsertSensorItemUseCase", "데이터 삭제 완료")
                fetchData()
            }
        }
    }

    fun saveBaselineValue(cmd: Char, o2Value: String, coValue: String, lelValue: String, co2Value: String, h2sValue: String) = viewModelScope.launch {
        insertBaselineItemUsecase(o2Value, coValue, lelValue, co2Value, h2sValue)
        Preferences.baselineEntity = BaselineEntity(
            id = 0,
            o2 = o2Value.toDouble(),
            lel = lelValue.toDouble(),
            co = coValue.toLong(),
            co2 = co2Value.toLong(),
            h2s = h2sValue.toLong()
        )
        if(Preferences.isMasterLevel) {
            sendSerialUseCase(Command.ALARM_SETTING ,cmd, "", o2Value, coValue, lelValue, co2Value, h2sValue)
        }
    }

    fun registrationEmergencyCall(name: String, num: String) = viewModelScope.launch {
        insertEmergencyCallItemUseCase(name, num)
        Preferences.showToast(SgmsApplication.appContext, "비상 연락처가 등록되었습니다.")
        fetchData()
    }
}