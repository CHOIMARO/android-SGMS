package com.tngen.sgms_android.utility

import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.util.Log
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.domain.serial.ConnectSerialUseCase
import com.tngen.sgms_android.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UsbReceiverImpl : UsbReceiver() {
    @Inject lateinit var connectSerialUseCase: ConnectSerialUseCase
    override fun onReceive(context: Context, intent: Intent) {

        super.onReceive(context, intent)

        if(UsbManager.ACTION_USB_DEVICE_ATTACHED == intent.action) {
            if (SgmsApplication.resumed) {
                GlobalScope.launch(Dispatchers.IO) {
                    connectSerialUseCase()

                }
            }
        }


        if(UsbManager.ACTION_USB_DEVICE_DETACHED == intent.action) {
//            val device: UsbDevice? = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE)
//            device?.apply {
//                // call your method that cleans up and closes communication with the device
//            }
        }
    }
}