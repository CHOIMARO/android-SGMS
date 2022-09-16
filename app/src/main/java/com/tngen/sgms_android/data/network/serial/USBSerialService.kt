package com.tngen.sgms_android.data.network.serial

import android.app.PendingIntent
import android.content.Context.USB_SERVICE
import android.content.Intent
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import android.util.Log
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import com.tngen.sgms_android.BuildConfig
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.utility.serial.SerialUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


class USBSerialService() : SerialInputOutputManager.Listener{
    private val _sharedFlow = MutableSharedFlow<ByteArray>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val sharedFlow = _sharedFlow.asSharedFlow()
//    private val INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".USB_PERMISSION"
    private val INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB"
    var port: UsbSerialPort? = null
    fun connect() {
        // Find all available drivers from attached devices.
        val manager = SgmsApplication.appContext?.getSystemService(USB_SERVICE) as UsbManager?
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
        if (availableDrivers.isEmpty()) {
            return
        }
        val usbConnection = requestConnection(manager!!, availableDrivers[0])
        if(usbConnection == null) {
            Log.d("USBSerialService", "usbConnection is Null")
            Timer("hello", false).schedule(1500) {
                val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager)
                if( availableDrivers[0] != null ) {
                    connect()
                }
            }
            return
        }
    }
    fun requestConnection(manager:UsbManager ,availableDrivers : UsbSerialDriver) : UsbDeviceConnection? {

        // Open a connection to the first available driver.
        val driver = availableDrivers
        var connection: UsbDeviceConnection? = null
        if (manager!!.hasPermission(driver.device)) {
            connection = manager.openDevice(driver.device)
//            connection = manager.openDevice(driver.device) ?: return
        }
        if (connection == null && !manager.hasPermission(driver.device)) {
            val usbPermissionIntent = PendingIntent.getBroadcast(
                SgmsApplication.appContext,
                0,
                Intent(INTENT_ACTION_GRANT_USB),
                PendingIntent.FLAG_IMMUTABLE
            )
            if (manager.deviceList.size > 0) {
                manager.requestPermission(driver.device, usbPermissionIntent)
            }
        } else {
            port = driver.ports[0]
            port!!.open(connection)
            port!!.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)

            val usbIoManager = SerialInputOutputManager(port, this)
            usbIoManager.start()

            Log.d("USBSerialService", "Connect Serial")
            port!!.write("Hello".toByteArray(), 2000)
        }
        return connection
    }

    fun send(byteArray: ByteArray) {
        port?.write(byteArray, 2000)
    }

    override fun onNewData(data: ByteArray?) {

        data!!.forEach {
//            SerialUtil.addDataString(String.format("%02X", it))
            SerialUtil.addDataString(it)
        }
        if(SerialUtil.isSerialReceived()) {
//            notifyObserversNewData(SerialUtil.dataByte)
            GlobalScope.launch(Dispatchers.IO) {
                _sharedFlow.emit(SerialUtil.dataByte)
                SerialUtil.clearDataString()
            }

        }

    }

    override fun onRunError(e: Exception?) {
        Log.d("USBSerialService", "시리얼 연결 끊김")
    }
}