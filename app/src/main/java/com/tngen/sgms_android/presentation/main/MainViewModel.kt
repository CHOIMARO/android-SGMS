package com.tngen.sgms_android.presentation.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tngen.sgms_android.data.entity.history.HistoryEntity
import com.tngen.sgms_android.data.entity.serial.SerialBaselineSettingResponseEntity
import com.tngen.sgms_android.data.entity.serial.SerialCalibrationSettingResponseEntity
import com.tngen.sgms_android.data.entity.serial.SerialEntity
import com.tngen.sgms_android.data.network.serial.USBSerialService
import com.tngen.sgms_android.domain.db.GetBaselineItemUseCase
import com.tngen.sgms_android.domain.db.InsertHistoryItemUseCase
import com.tngen.sgms_android.domain.serial.ConnectSerialUseCase
import com.tngen.sgms_android.domain.serial.ReceivedSerialUseCase
import com.tngen.sgms_android.preferences.Preferences
import com.tngen.sgms_android.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getBaselineItemUseCase: GetBaselineItemUseCase,
    private val connectSerialUseCase: ConnectSerialUseCase,
    private val receivedSerialUseCase: ReceivedSerialUseCase,
    private val insertHistoryItemUseCase: InsertHistoryItemUseCase,
    private val usbSerialService: USBSerialService,
) : BaseViewModel() {

    private var _mainStateLiveData = MutableLiveData<MainState>(MainState.UnInitialized)
    val mainStateLiveData: LiveData<MainState> = _mainStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        launch(Dispatchers.IO) {
            receivedSerialUseCase.sharedFlow.collect() {
                receivedSerialData(it)

            }
        }
        Preferences.baselineEntity = getBaselineItemUseCase(0)
        connectSerialUseCase()
        receivedSerialUseCase()

    }


    private fun receivedSerialData(byteArray: ByteArray) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("MainViewModel", "메세지 수신 중~~")
        if(byteArray.size == 15) {
            val serialEntity = SerialEntity (
                sensorId = String.format("%02X", byteArray[2]).toInt(16).toLong(),
                cmd = String.format("%02X", byteArray[3]).toInt(16).toChar(),
                o2 = String.format("%.1f",(String.format("%02X", byteArray[4]) + String.format("%02X", byteArray[5])).toInt(16).toDouble() * 0.1).toDouble(),
                co2 = (String.format("%02X", byteArray[6]) + String.format("%02X", byteArray[7])).toInt(16).toDouble(),
                co = (String.format("%02X", byteArray[8]) + String.format("%02X", byteArray[9])).toInt(16).toDouble(),
                h2s = (String.format("%02X", byteArray[10]) + String.format("%02X", byteArray[11])).toInt(16).toDouble(),
                lel = String.format("%02X", byteArray[12]).toInt(16).toDouble()
            )
            _mainStateLiveData.postValue(MainState.ReceivedSerialDataSuccess(serialEntity))
        }else if (byteArray.size == 6) {
            if(String.format("%02X", byteArray[3]).toInt(16).toChar() == 'c') {
                val serialCalibrationSettingResponseEntity = SerialCalibrationSettingResponseEntity (
                    sensorId = String.format("%02X", byteArray[2]).toInt(16).toLong(),
                    cmd = String.format("%02X", byteArray[3]).toInt(16).toChar()
                )
                _mainStateLiveData.postValue(MainState.ReceivedSerialCalibrationSettingSuccess(serialCalibrationSettingResponseEntity))
            }else if (String.format("%02X", byteArray[3]).toInt(16).toChar() == 'a') {
                val serialBaselineSettingResponseEntity = SerialBaselineSettingResponseEntity (
                    sensorId = String.format("%02X", byteArray[2]).toInt(16).toLong(),
                    cmd = String.format("%02X", byteArray[3]).toInt(16).toChar()
                )
                _mainStateLiveData.postValue(MainState.ReceivedSerialBaselineSettingSuccess(serialBaselineSettingResponseEntity))
            }
        }

    }

    fun insertHistoryItem(serialEntity: SerialEntity) = viewModelScope.launch{
        _mainStateLiveData.postValue(MainState.InsertHistoryItemSuccess(
            insertHistoryItemUseCase(HistoryEntity(
                sensorId = serialEntity.sensorId,
                o2 = serialEntity.o2,
                co2 = serialEntity.co2,
                co = serialEntity.co,
                h2s = serialEntity.h2s,
                lel = serialEntity.lel,
                createdAt = System.currentTimeMillis()
            ))
        ))

    }
}