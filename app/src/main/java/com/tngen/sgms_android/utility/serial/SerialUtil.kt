package com.tngen.sgms_android.utility.serial

import android.util.Log
import org.json.JSONException
import org.json.JSONObject

class SerialUtil {
    companion object {
        var dataByte = byteArrayOf()
        var serialFlag: Int = 0
        var json: JSONObject? = null


        @JvmStatic fun addDataString(byte: Byte) {
            val esc: Char = 27.toChar()

            dataByte += byte
            val pos = dataByte.indexOf("3C".toInt(16).toByte())
            if(serialFlag == 0 && pos > 0) {
                dataByte = dataByte.copyOfRange(pos, dataByte.size)
            }

            if(String.format("%02X", byte) == "3C" && dataByte.size == 1) {
                serialFlag +=1
            }
            if(String.format("%02X", byte) == "3E") {
                if(serialFlag > 0) {
                    serialFlag -=1
                }else{

                }
            }
            if (serialFlag < 0) clearDataString()
            if (dataByte.size > 30) clearDataString()
        }
        @JvmStatic fun clearDataString() {
            dataByte = byteArrayOf()
            serialFlag = 0
        }
        @JvmStatic fun isSerialReceived() : Boolean {
            val crc = CRC8()
            if (serialFlag == 0) {
                val ss = dataByte
                if(ss.size >= 4) {

                    if(String.format("%02X", ss[1]).equals("0B")) {
                        if(String.format("%02X", ss[3]).equals("72") || String.format("%02X", ss[3]).equals("63") || String.format("%02X", ss[3]).equals("61")) { //센서 데이터 수신 값 전달 Or 센서 캘리브레이션 설정 후 첫 메세지
                            if(ss.size >= 15) {
                                val recvData = ss.copyOfRange(0, 14)
                                crc.reset()
                                crc.update(recvData)
                                val value = crc.value

                                if(String.format("%02X", value) == String.format("%02X", ss[ss.lastIndex])) {
                                    dataByte = dataByte.copyOfRange(0,15)
                                    return true
                                }else{
                                    dataByte = byteArrayOf()
                                }
                            }
                        }
                    }else if (String.format("%02X", ss[1]).equals("02")) {
                        if (String.format("%02X", ss[3]).equals("63")) { //센서 캘리브레이션 설정 응답
                            if(ss.size >= 6) {
                                val recvData = ss.copyOfRange(0,5)
                                crc.reset()
                                crc.update(recvData)
                                val value = crc.value

                                if(String.format("%02X",value) == String.format("%02X", ss[ss.lastIndex])) {
                                    dataByte = dataByte.copyOfRange(0,6)
                                    return true
                                }else{
                                    dataByte = byteArrayOf()
                                }

                            }
                        } else if (String.format("%02X", ss[3]).equals("61")) { //알람 기준치 설정 응답
                            if(ss.size >= 6) {
                                val recvData = ss.copyOfRange(0,5)
                                crc.reset()
                                crc.update(recvData)
                                val value = crc.value

                                if(String.format("%02X", value) == String.format("%02X", ss[ss.lastIndex])) {
                                    dataByte = dataByte.copyOfRange(0,6)
                                    return true
                                }else{
                                    dataByte = byteArrayOf()
                                }
                            }
                        } else if (String.format("%02X", ss[3]).equals("6D")) {
                            if(ss.size >= 6) {
                                val recvData = ss.copyOfRange(0,5)
                                crc.reset()
                                crc.update(recvData)
                                val value = crc.value

                                if(String.format("%02X", value) == String.format("%02X", ss[ss.lastIndex])) {
                                    dataByte = dataByte.copyOfRange(0,6)
                                    return true
                                }else{
                                    dataByte = byteArrayOf()
                                }
                            }
                        } else if (String.format("%02X", ss[3]).equals("78")) {
                            Log.d("SerialUtil", "${ss[1]}번 센서 기능 고장 의심")
                            dataByte = byteArrayOf()
                        }
                    }

                }
            }
            return false
        }
//        @JvmStatic fun jsonComplete() : Boolean {
//            try {
//
//            } catch (e: JSONException) {
//
//
//            }
//            clearDataString()
//            return (json!=null)
//        }
    }
}