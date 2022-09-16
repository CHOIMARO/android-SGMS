package com.tngen.sgms_android.utility

import android.content.Context
import android.content.Intent
import android.util.Log
import com.tngen.sgms_android.SgmsApplication
import com.tngen.sgms_android.domain.serial.ConnectSerialUseCase
import com.tngen.sgms_android.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UsbReceiverImpl : UsbReceiver() {
    @Inject lateinit var connectSerialUseCase: ConnectSerialUseCase

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (SgmsApplication.resumed) {
            GlobalScope.launch(Dispatchers.IO) {
                connectSerialUseCase()
            }

        }
    }
}