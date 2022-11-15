package com.tngen.sgms_android.domain.serial

import android.util.Log
import com.tngen.sgms_android.data.network.serial.USBSerialService
import com.tngen.sgms_android.domain.UseCase
import com.tngen.sgms_android.utility.serial.CRC8
import com.tngen.sgms_android.utility.serial.enum.Command
import javax.inject.Inject

class SendSerialUseCase
    @Inject constructor(
    private val usbSerialService: USBSerialService
) : UseCase {
    operator fun invoke(command : Command ,cmd: Char, sensorId: String, o2Value: String, coValue: String, lelValue: String, co2Value: String, h2sValue: String) {
        usbSerialService.send(processingMessage(command, cmd, sensorId,o2Value,coValue,lelValue,co2Value,h2sValue))
    }
    fun connectCheckMessage(){
        usbSerialService.checkModuleLevel();
    }

    private fun processingMessage(command: Command, cmd: Char, sensorId: String, o2Value: String, coValue: String, lelValue: String, co2Value: String, h2sValue: String) : ByteArray {
        when(command) {
            Command.ALARM_SETTING -> {
                val crc = CRC8()
                crc.reset()
                var result = byteArrayOf()
                val cmdToByte = cmd.toByte()
//        sensorId = String.format("%02X", dataByte[2]).toInt(16).toLong(),
                val o2 = String.format("%04X",(o2Value.toDouble() * 10 ).toInt())
                val lel = String.format("%02X",(lelValue.toDouble()).toInt())
                val co = String.format("%04X",(coValue.toDouble().toInt()))
                val co2 = String.format("%04X",co2Value.toDouble().toInt())
                val h2s = String.format("%04X",h2sValue.toDouble().toInt())

                result = result +
                        '<'.toByte() +
                        "0B".toInt(16).toByte() + //길이
                        "FF".toInt(16).toByte() + //ID
                        cmdToByte +
                        o2.substring(0,2).toInt(16).toByte() + o2.substring(2).toInt(16).toByte() +
                        co2.substring(0,2).toInt(16).toByte() + co2.substring(2).toInt(16).toByte() +
                        co.substring(0,2).toInt(16).toByte() + co.substring(2).toInt(16).toByte() +
                        h2s.substring(0,2).toInt(16).toByte() + h2s.substring(2).toInt(16).toByte() +
                        lel.toInt(16).toByte() +
                        '>'.toByte()

                crc.update(result)
                result += crc.value.toByte()

                return result
            }
            Command.CALIBRATION_SETTING -> {
                val crc = CRC8()
                crc.reset()
                var result = byteArrayOf()
                val cmdToByte = cmd.toByte()
                val o2 = String.format("%04X",(o2Value.toDouble() * 10 ).toInt())
                val lel = String.format("%02X",(lelValue.toDouble()).toInt())
                val co = String.format("%04X",(coValue.toDouble().toInt()))
                val co2 = String.format("%04X",co2Value.toDouble().toInt())
                val h2s = String.format("%04X",h2sValue.toDouble().toInt())

                result = result +
                        '<'.toByte() +
                        "0B".toInt(16).toByte() + //길이
                        sensorId.toInt(16).toByte() + //ID
                        cmdToByte +
                        o2.substring(0,2).toInt(16).toByte() + o2.substring(2).toInt(16).toByte() +
                        co2.substring(0,2).toInt(16).toByte() + co2.substring(2).toInt(16).toByte() +
                        co.substring(0,2).toInt(16).toByte() + co.substring(2).toInt(16).toByte() +
                        h2s.substring(0,2).toInt(16).toByte() + h2s.substring(2).toInt(16).toByte() +
                        lel.toInt(16).toByte() +
                        '>'.toByte()

                crc.update(result)
                result += crc.value.toByte()

                return result
            }
        }
    }
}